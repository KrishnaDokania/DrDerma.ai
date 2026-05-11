package ai.drderma.backend.service;

import ai.drderma.backend.engine.DiseaseKnowledgeBase;
import ai.drderma.backend.model.*;
import ai.drderma.backend.questions.FeatureRepository;
import ai.drderma.backend.questions.QuestionEngine;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TriageService {

    private static final long SESSION_TIMEOUT_MS =
            10 * 60 * 1000;

    private final DiseaseKnowledgeBase kb;

    private final FeatureRepository featureRepository;

    private final Map<String, TriageSession>
            sessions =
            new ConcurrentHashMap<>();

    public TriageService(

            DiseaseKnowledgeBase kb,

            FeatureRepository featureRepository
    ) {

        this.kb = kb;

        this.featureRepository =
                featureRepository;
    }

    // =====================================================
    // START TRIAGE
    // =====================================================

    public Map<String, Object> start(

            List<ImageCandidate> candidates
    ) {

        TriageSession session =
                new TriageSession(
                        candidates
                );

        sessions.put(

                session.getSessionId(),

                session
        );

        return next(session);
    }

    // =====================================================
    // ANSWER QUESTION
    // =====================================================

    public Map<String, Object> answer(

            String sessionId,

            String signal,

            String answer
    ) {

        TriageSession session =
                sessions.get(sessionId);

        if (session == null) {

            return Map.of(
                    "error",
                    "Session not found"
            );
        }

        if (
                System.currentTimeMillis()
                        - session.getCreatedAt()
                        > SESSION_TIMEOUT_MS
        ) {

            sessions.remove(sessionId);

            return Map.of(
                    "error",
                    "Session expired"
            );
        }

        signal = signal.toLowerCase();

        answer = answer.toLowerCase();

        session.getAskedSignals()
                .add(signal);

        applyAnswer(

                session,

                signal,

                answer
        );

        return next(session);
    }

    // =====================================================
    // APPLY ANSWER
    // =====================================================

    private void applyAnswer(

            TriageSession session,

            String signal,

            String answer
    ) {

        for (CandidateState candidate :
                session.getCandidates()) {

            DiseaseProfile profile =
                    kb.get(
                            candidate.getDisease()
                    );

            if (profile == null) {
                continue;
            }

            int delta =

                    profile.getSignalWeights()

                            .getOrDefault(
                                    signal,
                                    Map.of()
                            )

                            .getOrDefault(
                                    answer,
                                    0
                            );

            candidate.addQuestionScore(
                    delta
            );

            candidate.recordImpact(
                    signal,
                    delta
            );

            // =====================================
            // REASONS
            // =====================================

            if (delta != 0) {

                session.getReasons()

                        .get(
                                candidate.getDisease()
                        )

                        .add(
                                signal
                                        + " = "
                                        + answer
                        );
            }
        }
    }

    // =====================================================
    // NEXT STEP
    // =====================================================

    private Map<String, Object> next(
            TriageSession session
    ) {

        List<CandidateState> ranked =

                session.getCandidates()

                        .stream()

                        .filter(candidate ->

                                !session
                                        .getEliminatedDiseases()
                                        .contains(
                                                candidate.getDisease()
                                        )
                        )

                        .sorted((a, b) ->

                                Double.compare(

                                        b.getFinalScore(),

                                        a.getFinalScore()
                                )
                        )

                        .collect(Collectors.toList());

        eliminateWeakCandidates(
                ranked
        );

        // =====================================
        // STOP CONDITION
        // =====================================

        if (
                shouldStop(ranked)
        ) {

            return buildFinalResult(

                    session,

                    ranked
            );
        }

        // =====================================
        // NEXT QUESTION
        // =====================================

        String nextQuestion =

                QuestionEngine
                        .selectNextQuestion(

                                ranked,

                                kb,

                                session
                                        .getAskedSignals()
                        );

        if (nextQuestion == null) {

            return buildFinalResult(

                    session,

                    ranked
            );
        }

        Feature feature =
                featureRepository
                        .getFeature(
                                nextQuestion
                        );

        Map<String, Object>
                response =
                new HashMap<>();

        response.put(

                "sessionId",

                session.getSessionId()
        );

        response.put(
                "question",
                feature
        );

        response.put(

                "activeDiseases",

                ranked.stream()

                        .limit(3)

                        .map(
                                CandidateState::getDisease
                        )

                        .toList()
        );

        return response;
    }

    // =====================================================
    // ELIMINATION
    // =====================================================

    private void eliminateWeakCandidates(
            List<CandidateState> ranked
    ) {

        if (ranked.isEmpty()) {
            return;
        }

        double top =
                ranked.get(0)
                        .getFinalScore();

        ranked.removeIf(candidate -> {

            double diff =
                    top
                            - candidate
                            .getFinalScore();

            return diff > 35;
        });
    }

    // =====================================================
    // STOPPING
    // =====================================================

    private boolean shouldStop(
            List<CandidateState> ranked
    ) {

        if (ranked.size() <= 1) {
            return true;
        }

        double top =
                ranked.get(0)
                        .getFinalScore();

        double second =
                ranked.get(1)
                        .getFinalScore();

        double gap = top - second;

        return gap >= 25;
    }

    // =====================================================
    // FINAL RESULT
    // =====================================================

    private Map<String, Object>
    buildFinalResult(

            TriageSession session,

            List<CandidateState> ranked
    ) {

        CandidateState top =
                ranked.get(0);

        CandidateState second =
                ranked.size() > 1
                        ? ranked.get(1)
                        : top;

        double confidence =
                calculateConfidence(
                        top,
                        second
                );

        Map<String, Object>
                mostLikely =
                new HashMap<>();

        mostLikely.put(
                "disease",
                top.getDisease()
        );

        mostLikely.put(
                "confidence",
                confidence
        );

        mostLikely.put(
                "why",

                session.getReasons()
                        .get(
                                top.getDisease()
                        )
        );

        List<Map<String, Object>>
                alternatives =

                ranked.stream()

                        .skip(1)

                        .limit(3)

                        .map(candidate -> {

                            Map<String, Object>
                                    alt =
                                    new HashMap<>();

                            alt.put(
                                    "disease",
                                    candidate.getDisease()
                            );

                            alt.put(
                                    "confidence",

                                    Math.max(
                                            confidence
                                                    - 15,
                                            5
                                    )
                            );

                            return alt;
                        })

                        .toList();

        Map<String, Object>
                response =
                new HashMap<>();

        response.put(
                "mostLikely",
                mostLikely
        );

        response.put(
                "alternatives",
                alternatives
        );

        response.put(
                "finished",
                true
        );

        return response;
    }

    // =====================================================
    // CONFIDENCE
    // =====================================================

    private double calculateConfidence(

            CandidateState top,

            CandidateState second
    ) {

        double gap =
                top.getFinalScore()
                        - second.getFinalScore();

        double confidence =

                100 *
                        (
                                1
                                        - Math.exp(
                                        -gap / 15
                                )
                        );

        if (confidence > 99) {
            confidence = 99;
        }

        if (confidence < 35) {
            confidence = 35;
        }

        return confidence;
    }
}
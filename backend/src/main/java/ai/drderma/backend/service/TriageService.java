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

    private static final long
            SESSION_TIMEOUT_MS =
            10 * 60 * 1000;

    private final DiseaseKnowledgeBase
            kb;

    private final FeatureRepository
            featureRepository;

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

        // SESSION EXPIRY

        if (
                System.currentTimeMillis()
                        - session.getCreatedAt()
                        > SESSION_TIMEOUT_MS
        ) {

            sessions.remove(
                    sessionId
            );

            return Map.of(
                    "error",
                    "Session expired"
            );
        }

        signal =
                signal.toLowerCase();

        answer =
                answer.toLowerCase();

        session.getAskedSignals()
                .add(signal);

        applyAnswer(

                session,

                signal,

                answer
        );

        rerankCandidates(session);

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

        for (
                CandidateState candidate :
                session.getCandidates()
        ) {

            DiseaseProfile profile =
                    kb.get(
                            candidate.getDisease()
                    );

            if (profile == null) {
                continue;
            }

            Map<String, Map<String, Integer>>
                    signals =
                    profile.getSignalWeights();

            if (
                    !signals.containsKey(signal)
            ) {

                continue;
            }

            int delta =
                    signals.get(signal)

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
    // RERANK
    // =====================================================

    private void rerankCandidates(
            TriageSession session
    ) {

        session.getCandidates()

                .sort((a, b) ->

                        Double.compare(

                                b.getFinalScore(),

                                a.getFinalScore()
                        )
                );
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

                        .sorted((a, b) ->

                                Double.compare(

                                        b.getFinalScore(),

                                        a.getFinalScore()
                                )
                        )

                        .collect(
                                Collectors.toList()
                        );

        // =====================================
        // REMOVE VERY WEAK DISEASES
        // =====================================

        eliminateWeakCandidates(
                ranked
        );

        // =====================================
        // STOPPING CONDITION
        // =====================================

        if (

                session.getAskedSignals()
                        .size() >= 6

                        &&

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

        // SAFETY

        if (feature == null) {

            return buildFinalResult(

                    session,

                    ranked
            );
        }

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

                        .map(candidate -> {

                            Map<String, Object>
                                    disease =
                                    new HashMap<>();

                            disease.put(
                                    "disease",
                                    candidate.getDisease()
                            );

                            disease.put(
                                    "score",
                                    candidate.getFinalScore()
                            );

                            return disease;
                        })

                        .collect(
                                Collectors.toList()
                        )
        );

        return response;
    }

    // =====================================================
    // ELIMINATION
    // =====================================================

    private void eliminateWeakCandidates(
            List<CandidateState> ranked
    ) {

        if (
                ranked.isEmpty()
        ) {

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

            return diff > 2.5;
        });
    }

    // =====================================================
    // STOPPING
    // =====================================================

    private boolean shouldStop(
            List<CandidateState> ranked
    ) {

        if (
                ranked == null
                        ||
                        ranked.size() < 2
        ) {

            return false;
        }

        double top =
                ranked.get(0)
                        .getFinalScore();

        double second =
                ranked.get(1)
                        .getFinalScore();

        double gap =
                top - second;

        return gap >= 5.0;
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

        double gap =
                top.getFinalScore()
                        - second.getFinalScore();

        double confidence =

                Math.min(

                        95,

                        45

                                +

                                (
                                        gap * 12
                                )

                                +

                                (
                                        session
                                                .getAskedSignals()
                                                .size() * 4
                                )
                );

        Map<String, Object>
                mostLikely =
                new HashMap<>();

        mostLikely.put(
                "disease",
                top.getDisease()
        );
        mostLikely.put(
        "description",
        getDiseaseDescription(
                top.getDisease()
        )
);

mostLikely.put(
        "medications",
        getMedicationSuggestions(
                top.getDisease()
        )
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

                                            confidence - 15,

                                            5
                                    )
                            );

                            return alt;
                        })

                        .collect(
                                Collectors.toList()
                        );

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
    private String getDiseaseDescription(
        String disease
) {

    switch (disease) {

        case "tinea_corporis":

            return
                    "Tinea corporis is a superficial fungal infection "
                    + "commonly known as ringworm. It typically presents "
                    + "as circular, itchy, red patches with raised borders "
                    + "and central clearing. The condition spreads through "
                    + "direct skin contact, contaminated surfaces, or moist environments.";

        case "psoriasis_vulgaris":

            return
                    "Psoriasis vulgaris is a chronic autoimmune skin disorder "
                    + "characterized by thick, red, scaly plaques covered "
                    + "with silvery-white scales. It commonly affects the scalp, "
                    + "elbows, knees, and trunk and may fluctuate in severity over time.";

        case "eczema_atopic_dermatitis":

            return
                    "Atopic dermatitis (eczema) is an inflammatory skin condition "
                    + "causing dry, itchy, inflamed, and sometimes cracked skin. "
                    + "It is commonly associated with allergies, asthma, or sensitive skin.";

        case "acne_vulgaris":

            return
                    "Acne vulgaris is a common skin condition involving clogged pores, "
                    + "oil overproduction, blackheads, whiteheads, and inflamed pimples. "
                    + "It most commonly affects the face, chest, shoulders, and back.";

        case "varicella_chickenpox":

            return
                    "Chickenpox (varicella) is a highly contagious viral infection "
                    + "characterized by itchy, fluid-filled blisters and widespread rash. "
                    + "It is often accompanied by fever, fatigue, and body aches.";

        default:

            return
                    "AI-generated clinical assessment.";
    }
}
private List<Map<String, String>>
getMedicationSuggestions(
        String disease
) {

    List<Map<String, String>>
            medications =
            new ArrayList<>();

    switch (disease) {

        // =====================================
        // TINEA
        // =====================================

        case "tinea_corporis":

            medications.add(
                    Map.of(
                            "name",
                            "Clotrimazole 1% Cream",

                            "usage",
                            "Apply twice daily for 2-4 weeks"
                    )
            );

            medications.add(
                    Map.of(
                            "name",
                            "Terbinafine Cream",

                            "usage",
                            "Apply once daily to affected area"
                    )
            );

            break;

        // =====================================
        // PSORIASIS
        // =====================================

        case "psoriasis_vulgaris":

            medications.add(
                    Map.of(
                            "name",
                            "Topical Corticosteroids",

                            "usage",
                            "Apply thin layer once daily"
                    )
            );

            medications.add(
                    Map.of(
                            "name",
                            "Salicylic Acid",

                            "usage",
                            "Helps reduce scaling"
                    )
            );

            break;

        // =====================================
        // ECZEMA
        // =====================================

        case "eczema_atopic_dermatitis":

            medications.add(
                    Map.of(
                            "name",
                            "Hydrocortisone Cream",

                            "usage",
                            "Apply 1-2 times daily"
                    )
            );

            medications.add(
                    Map.of(
                            "name",
                            "Moisturizing Cream",

                            "usage",
                            "Use frequently to restore skin barrier"
                    )
            );

            break;

        // =====================================
        // ACNE
        // =====================================

        case "acne_vulgaris":

            medications.add(
                    Map.of(
                            "name",
                            "Benzoyl Peroxide",

                            "usage",
                            "Apply once daily"
                    )
            );

            medications.add(
                    Map.of(
                            "name",
                            "Adapalene Gel",

                            "usage",
                            "Apply at night"
                    )
            );

            break;

        // =====================================
        // CHICKENPOX
        // =====================================

        case "varicella_chickenpox":

            medications.add(
                    Map.of(
                            "name",
                            "Calamine Lotion",

                            "usage",
                            "Apply to reduce itching and irritation"
                    )
            );

            medications.add(
                    Map.of(
                            "name",
                            "Paracetamol",

                            "usage",
                            "Used to manage fever and discomfort"
                    )
            );

            break;
    }

    return medications;
}
}
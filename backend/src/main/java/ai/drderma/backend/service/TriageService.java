package ai.drderma.backend.service;

import ai.drderma.backend.engine.DiseaseKnowledgeBase;
import ai.drderma.backend.engine.QuestionSelector;
import ai.drderma.backend.model.DiseaseProfile;
import ai.drderma.backend.model.ImageCandidate;
import ai.drderma.backend.model.TriageSession;
import ai.drderma.backend.registry.SignalQuestionRegistry;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TriageService {

    private static final long SESSION_TIMEOUT_MS = 10 * 60 * 1000;
    private static final double MIN_SCORE_TO_DECIDE = 2.5;
    private static final double MIN_SCORE_GAP = 0.5;

    private static final String DISCLAIMER =
            "This system provides decision support only and is not a medical diagnosis";

    private final DiseaseKnowledgeBase kb;
    private final QuestionSelector selector;
    private final SignalQuestionRegistry registry;

    private final Map<String, TriageSession> sessions = new ConcurrentHashMap<>();

    public TriageService(
            DiseaseKnowledgeBase kb,
            QuestionSelector selector,
            SignalQuestionRegistry registry
    ) {
        this.kb = kb;
        this.selector = selector;
        this.registry = registry;
    }

    public Map<String, Object> start(List<ImageCandidate> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return Map.of("error", "No image candidates provided");
        }

        TriageSession session = new TriageSession(candidates);
        sessions.put(session.getSessionId(), session);
        return next(session);
    }

    public Map<String, Object> answer(
            String sessionId,
            String signal,
            String answer
    ) {
        signal = signal.toLowerCase();
        answer = answer.toLowerCase();

        TriageSession session = sessions.get(sessionId);

        if (session == null) {
            return Map.of("error", "Invalid or expired session");
        }

        if (System.currentTimeMillis() - session.getCreatedAt() > SESSION_TIMEOUT_MS) {
            sessions.remove(sessionId);
            return Map.of("error", "Session expired. Please restart triage.");
        }

        session.getAskedSignals().add(signal);
        applyAnswerToScores(session, signal, answer);

        return next(session);
    }

    private void applyAnswerToScores(
            TriageSession session,
            String signal,
            String answer
    ) {
        for (String disease : session.getScores().keySet()) {

            DiseaseProfile profile = kb.get(disease);
            if (profile == null) continue;

            int weight =
                    profile.getSignalWeights()
                            .getOrDefault(signal, Map.of())
                            .getOrDefault(answer, 0);

            double updatedScore =
                    session.getScores().get(disease) + weight;

            session.getScores().put(disease, updatedScore);

            if (weight != 0) {
                session.getReasons()
                        .get(disease)
                        .add(signal + " = " + answer);
            }
        }
    }

    private Map<String, Object> next(TriageSession session) {

        List<Map.Entry<String, Double>> ranked =
                session.getScores().entrySet()
                        .stream()
                        .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                        .toList();

        if (ranked.isEmpty()) {
            return Map.of(
                    "status", "error",
                    "message", "No diseases available for evaluation"
            );
        }

        double topScore = ranked.get(0).getValue();
        double secondScore = ranked.size() > 1 ? ranked.get(1).getValue() : 0.0;

        double totalPositiveScore =
                ranked.stream()
                        .mapToDouble(Map.Entry::getValue)
                        .filter(v -> v > 0)
                        .sum();

        boolean confident =
                topScore >= MIN_SCORE_TO_DECIDE &&
                (topScore - secondScore) >= MIN_SCORE_GAP;

        if (confident) {

            Map<String, Object> mostLikely = new HashMap<>();
            mostLikely.put("disease", ranked.get(0).getKey());
            mostLikely.put(
                    "confidence",
                    totalPositiveScore == 0 ? 0 :
                            Math.min((topScore * 100.0) / totalPositiveScore, 90.0)
            );
            mostLikely.put(
                    "why",
                    session.getReasons().get(ranked.get(0).getKey())
            );

            List<Map<String, Object>> alternatives =
                    ranked.stream()
                            .skip(1)
                            .filter(e -> e.getValue() > 0)
                            .map(e -> {
                                Map<String, Object> m = new HashMap<>();
                                m.put("disease", e.getKey());
                                m.put(
                                        "confidence",
                                        totalPositiveScore == 0 ? 0 :
                                                (e.getValue() * 100.0) / totalPositiveScore
                                );
                                return m;
                            })
                            .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("mostLikely", mostLikely);
            response.put("alternatives", alternatives);
            response.put("note", DISCLAIMER);

            return response;
        }

        List<DiseaseProfile> profiles =
                ranked.stream()
                        .limit(3)
                        .map(e -> kb.get(e.getKey()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        String nextSignal =
                selector.selectBestSignal(
                        profiles,
                        session.getAskedSignals()
                );

        if (nextSignal == null) {
            return Map.of(
                    "status", "uncertain",
                    "message",
                    "No more distinguishing questions available",
                    "rankedResults", ranked
            );
        }

       Map<String, Object> question = registry.getQuestion(nextSignal);

        if (question == null) {
    return Map.of(
        "status", "uncertain",
        "message",
        "Missing question for signal: " + nextSignal,
        "rankedResults", ranked
    );
}

Map<String, Object> response = new HashMap<>();
response.put("sessionId", session.getSessionId());
response.put("question", question);

return response;

    }
}

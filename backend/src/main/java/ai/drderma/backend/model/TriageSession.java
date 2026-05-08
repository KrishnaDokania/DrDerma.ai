package ai.drderma.backend.model;
import ai.drderma.backend.model.CandidateState;
import java.util.*;

public class TriageSession {
private final Set<String> eliminatedDiseases =
        new HashSet<>();
    private final String sessionId;
    private final Map<String, Double> scores = new HashMap<>();
    private final Set<String> askedSignals = new HashSet<>();
    private final Map<String, List<String>> reasons = new HashMap<>();
    private final long createdAt = System.currentTimeMillis();

    public TriageSession(List<ImageCandidate> candidates) {
        this.sessionId = UUID.randomUUID().toString();

        for (ImageCandidate c : candidates) {

            // Use similarity directly as base score
            double baseScore = c.getSimilarity();

            scores.put(c.getDisease(), baseScore);

            reasons.put(
                c.getDisease(),
                new ArrayList<>(
                    List.of("Initial image similarity: " + c.getSimilarity())
                )
            );
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    public Map<String, Double> getScores() {
        return scores;
    }

    public Set<String> getAskedSignals() {
        return askedSignals;
    }

    public Map<String, List<String>> getReasons() {
        return reasons;
    }

    public long getCreatedAt() {
        return createdAt;
    }
    public List<CandidateState> toCandidateStates() {

    List<CandidateState> list =
            new ArrayList<>();

    for (Map.Entry<String, Double> entry :
            scores.entrySet()) {

        CandidateState state =
                new CandidateState(
                        entry.getKey(),
                        entry.getValue()
                );

        list.add(state);
    }

    return list;
}
public Set<String> getEliminatedDiseases() {
    return eliminatedDiseases;
}
}

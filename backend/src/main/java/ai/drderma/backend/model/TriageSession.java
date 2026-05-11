package ai.drderma.backend.model;

import java.util.*;

public class TriageSession {

    private final String sessionId;

    private final long createdAt;

    // ACTIVE CANDIDATES

    private final List<CandidateState>
            candidates =
            new ArrayList<>();

    // ASKED QUESTIONS

    private final Set<String>
            askedSignals =
            new HashSet<>();

    // ELIMINATED DISEASES

    private final Set<String>
            eliminatedDiseases =
            new HashSet<>();

    // REASON TRACKING

    private final Map<String, List<String>>
            reasons =
            new HashMap<>();

    public TriageSession(
            List<ImageCandidate> imageCandidates
    ) {

        this.sessionId =
                UUID.randomUUID().toString();

        this.createdAt =
                System.currentTimeMillis();

        for (ImageCandidate image :
                imageCandidates) {

            double baseScore =
                    image.getSimilarity() != null
                            ? image.getSimilarity()
                            : 0.0;

            CandidateState state =
                    new CandidateState(

                            image.getDisease(),

                            baseScore
                    );

            // =====================================
            // VISUAL TRAITS
            // =====================================

            state.setVisualTraits(
                    image.getTraits()
            );

            candidates.add(state);

            reasons.put(

                    image.getDisease(),

                    new ArrayList<>(
                            List.of(
                                    "Initial image similarity: "
                                            + baseScore
                            )
                    )
            );
        }
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public String getSessionId() {
        return sessionId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public List<CandidateState>
    getCandidates() {

        return candidates;
    }

    public Set<String>
    getAskedSignals() {

        return askedSignals;
    }

    public Set<String>
    getEliminatedDiseases() {

        return eliminatedDiseases;
    }

    public Map<String, List<String>>
    getReasons() {

        return reasons;
    }
}
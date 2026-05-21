package ai.drderma.backend.model;

import java.util.HashMap;
import java.util.Map;

public class CandidateState {

    // =====================================================
    // DISEASE
    // =====================================================

    private final String disease;

    // =====================================================
    // IMAGE SCORE
    // =====================================================

    private final double similarityScore;

    // =====================================================
    // QUESTION SCORE
    // =====================================================

    private double questionScore = 0;

    // =====================================================
    // TRACK QUESTION IMPACT
    // =====================================================

    private final Map<String, Integer>
            questionImpact =
            new HashMap<>();

    // =====================================================
    // VISUAL TRAITS
    // =====================================================

    private VisualTraits visualTraits;

    // =====================================================
    // CONTRADICTION SCORE
    // =====================================================

    private double contradictionPenalty =
            0;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public CandidateState(

            String disease,

            double similarityScore
    ) {

        this.disease =
                disease;

        this.similarityScore =
                similarityScore;
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public String getDisease() {
        return disease;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public double getQuestionScore() {
        return questionScore;
    }

    public VisualTraits getVisualTraits() {
        return visualTraits;
    }

    public double getContradictionPenalty() {
        return contradictionPenalty;
    }

    public Map<String, Integer>
    getQuestionImpact() {

        return questionImpact;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setVisualTraits(
            VisualTraits visualTraits
    ) {

        this.visualTraits =
                visualTraits;
    }

    // =====================================================
    // QUESTION SCORE
    // =====================================================

    public void addQuestionScore(
            double delta
    ) {

        questionScore += delta;

        // =====================================
        // CONTRADICTION DETECTION
        // =====================================

        if (delta < 0) {

            contradictionPenalty +=
                    Math.abs(delta) * 0.35;
        }
    }

    // =====================================================
    // RECORD IMPACT
    // =====================================================

    public void recordImpact(

            String question,

            int delta
    ) {

        questionImpact.put(
                question,
                delta
        );
    }

    // =====================================================
    // FINAL SCORE
    // =====================================================

    public double getFinalScore() {

        // =====================================
        // IMAGE WEIGHT
        // =====================================

        double imageComponent =
                similarityScore * 0.65;

        // =====================================
        // QUESTION WEIGHT
        // =====================================

        double questionComponent =
                normalizeQuestionScore()
                        * 0.35;

        // =====================================
        // CONTRADICTION PENALTY
        // =====================================

        double contradictionComponent =
                contradictionPenalty;

        return

                imageComponent

                        +

                        questionComponent

                        -

                        contradictionComponent;
    }

    // =====================================================
    // NORMALIZE
    // =====================================================

    private double normalizeQuestionScore() {

        double normalized =
                questionScore / 12.0;

        // SOFT CLAMP

        if (normalized > 1.0) {
            return 1.0;
        }

        if (normalized < -1.0) {
            return -1.0;
        }

        return normalized;
    }
}
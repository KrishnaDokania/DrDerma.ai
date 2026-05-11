package ai.drderma.backend.model;

import java.util.*;

public class CandidateState {

    private final String disease;

    private final double similarityScore;

    private VisualTraits visualTraits;

    private double questionScore = 0.0;

    private final Map<String, Integer>
            questionImpact =
            new HashMap<>();

    private int questionsAnswered = 0;

    private double contradictionPenalty = 0.0;

    public CandidateState(
            String disease,
            double similarityScore
    ) {

        this.disease = disease;

        this.similarityScore =
                similarityScore;
    }

    public String getDisease() {
        return disease;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public VisualTraits getVisualTraits() {
        return visualTraits;
    }

    public void setVisualTraits(
            VisualTraits visualTraits
    ) {

        this.visualTraits = visualTraits;
    }

    public double getQuestionScore() {
        return questionScore;
    }

    public void addQuestionScore(
            double delta
    ) {

        questionScore += delta;

        questionsAnswered++;

        if (delta < 0) {

            contradictionPenalty +=
                    Math.abs(delta) * 0.5;
        }
    }

    public void recordImpact(
            String question,
            int delta
    ) {

        questionImpact.put(
                question,
                delta
        );
    }

    public Map<String, Integer>
    getQuestionImpact() {

        return questionImpact;
    }

    public double getFinalScore() {

        double imageWeight;

        double questionWeight;

        if (questionsAnswered <= 2) {

            imageWeight = 0.75;
            questionWeight = 0.25;

        }

        else if (questionsAnswered <= 5) {

            imageWeight = 0.55;
            questionWeight = 0.45;
        }

        else {

            imageWeight = 0.35;
            questionWeight = 0.65;
        }

        double weightedImage =
                similarityScore * imageWeight;

        double weightedQuestions =
                normalizeQuestionScore()
                        * questionWeight;

        return weightedImage
                + weightedQuestions
                - contradictionPenalty;
    }

    private double normalizeQuestionScore() {

        double normalized =
                questionScore / 10.0;

        if (normalized > 1.0) {
            return 1.0;
        }

        if (normalized < -1.0) {
            return -1.0;
        }

        return normalized;
    }
}
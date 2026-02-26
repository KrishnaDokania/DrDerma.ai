package ai.drderma.backend.model;
import java.util.*;

public class CandidateState {

    private final String disease;
    private final double similarityScore;
private final Map<String, Integer> questionImpact = new HashMap<>();
    private double questionScore = 0.0;

    public CandidateState(String disease, double similarityScore) {
        this.disease = disease;
        this.similarityScore = similarityScore;
    }

    public String getDisease() {
        return disease;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public double getQuestionScore() {
        return questionScore;
    }

    public void addQuestionScore(double delta) {
        this.questionScore += delta;
    }

    public double getFinalScore() {
        // 60% image similarity
        // 40% question reasoning
        return 0.6 * similarityScore + 0.4 * normalizeQuestionScore();
    }

 private double normalizeQuestionScore() {
    // Soft clamp
    double normalized = questionScore / 10.0;

    if (normalized > 0.5) return 0.5;
    if (normalized < -0.5) return -0.5;

    return normalized;
}
public void recordImpact(String question, int delta) {
    questionImpact.put(question, delta);
}
public Map<String, Integer> getQuestionImpact() {
    return questionImpact;
}
public void setQuestionImpact(Map<String, Integer> impact) {
    this.questionImpact.clear();
    this.questionImpact.putAll(impact);
}
}

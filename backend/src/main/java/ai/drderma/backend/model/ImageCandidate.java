package ai.drderma.backend.model;

public class ImageCandidate {

    private String disease;
    private double similarity;

    // REQUIRED
    public ImageCandidate() {
    }

    public ImageCandidate(String disease, double similarity) {
        this.disease = disease;
        this.similarity = similarity;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}

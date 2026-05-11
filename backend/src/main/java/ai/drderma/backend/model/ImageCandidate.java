package ai.drderma.backend.model;

public class ImageCandidate {

    private String disease;

    private Double similarity;

    private VisualTraits traits;

    public ImageCandidate() {
    }

    public ImageCandidate(
            String disease,
            Double similarity
    ) {
        this.disease = disease;
        this.similarity = similarity;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public VisualTraits getTraits() {
        return traits;
    }

    public void setTraits(VisualTraits traits) {
        this.traits = traits;
    }
}
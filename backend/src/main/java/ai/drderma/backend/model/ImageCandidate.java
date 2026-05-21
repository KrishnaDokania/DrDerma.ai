package ai.drderma.backend.model;

public class ImageCandidate {

    // =====================================================
    // DISEASE
    // =====================================================

    private String disease;

    // =====================================================
    // IMAGE SIMILARITY
    // =====================================================

    private Double similarity;

    // =====================================================
    // VISUAL TRAITS
    // =====================================================

    private VisualTraits traits;

    // =====================================================
    // CONSTRUCTORS
    // =====================================================

    public ImageCandidate() {
    }

    public ImageCandidate(

            String disease,

            Double similarity
    ) {

        this.disease =
                disease;

        this.similarity =
                similarity;
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public String getDisease() {
        return disease;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public VisualTraits getTraits() {
        return traits;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setDisease(
            String disease
    ) {

        this.disease =
                disease;
    }

    public void setSimilarity(
            Double similarity
    ) {

        this.similarity =
                similarity;
    }

    public void setTraits(
            VisualTraits traits
    ) {

        this.traits =
                traits;
    }
}
package ai.drderma.backend.model;

import java.util.ArrayList;
import java.util.List;

public class VisualTraits {

    // =====================================================
    // MORPHOLOGY
    // =====================================================

    private List<String> morphology =
            new ArrayList<>();

    // =====================================================
    // TEXTURE
    // =====================================================

    private List<String> textures =
            new ArrayList<>();

    // =====================================================
    // COLORS
    // =====================================================

    private List<String> colors =
            new ArrayList<>();

    // =====================================================
    // DISTRIBUTION
    // =====================================================

    private List<String> distribution =
            new ArrayList<>();

    // =====================================================
    // BORDER FEATURES
    // =====================================================

    private List<String> borderFeatures =
            new ArrayList<>();

    // =====================================================
    // SURFACE FEATURES
    // =====================================================

    private List<String> surfaceFeatures =
            new ArrayList<>();

    // =====================================================
    // GETTERS
    // =====================================================

    public List<String> getMorphology() {
        return morphology;
    }

    public List<String> getTextures() {
        return textures;
    }

    public List<String> getColors() {
        return colors;
    }

    public List<String> getDistribution() {
        return distribution;
    }

    public List<String> getBorderFeatures() {
        return borderFeatures;
    }

    public List<String> getSurfaceFeatures() {
        return surfaceFeatures;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setMorphology(
            List<String> morphology
    ) {

        this.morphology =
                morphology;
    }

    public void setTextures(
            List<String> textures
    ) {

        this.textures =
                textures;
    }

    public void setColors(
            List<String> colors
    ) {

        this.colors =
                colors;
    }

    public void setDistribution(
            List<String> distribution
    ) {

        this.distribution =
                distribution;
    }

    public void setBorderFeatures(
            List<String> borderFeatures
    ) {

        this.borderFeatures =
                borderFeatures;
    }

    public void setSurfaceFeatures(
            List<String> surfaceFeatures
    ) {

        this.surfaceFeatures =
                surfaceFeatures;
    }
}
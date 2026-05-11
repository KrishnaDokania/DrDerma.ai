package ai.drderma.backend.model;

import java.util.List;

public class VisualTraits {

    private List<String> morphology;

    private List<String> colors;

    private List<String> textures;

    private List<String> locations;

    public List<String> getMorphology() {
        return morphology;
    }

    public void setMorphology(List<String> morphology) {
        this.morphology = morphology;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<String> getTextures() {
        return textures;
    }

    public void setTextures(List<String> textures) {
        this.textures = textures;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
package ai.drderma.backend.model;

import java.util.List;
import java.util.Map;

public class DiseaseProfile {

    // =====================================================
    // BASIC INFO
    // =====================================================

    private final String disease;

    // =====================================================
    // DISEASE CATEGORY
    // =====================================================

    // fungal
    // inflammatory
    // autoimmune
    // acneiform
    // pigmentary

    private final String category;

    // =====================================================
    // VISUAL FAMILY
    // =====================================================

    // annular_fungal
    // scaly_plaque
    // inflammatory_patch
    // papulopustular
    // pigmentary

    private final String visualFamily;

    // =====================================================
    // COMMON BODY LOCATIONS
    // =====================================================

    private final List<String>
            bodyLocations;

    // =====================================================
    // MORPHOLOGY
    // =====================================================

    private final List<String>
            morphology;

    // =====================================================
    // DIAGNOSTIC IMPORTANCE
    // =====================================================

    private final int rarityWeight;

    // =====================================================
    // SIGNAL WEIGHTS
    // =====================================================

    private final Map<
            String,
            Map<String, Integer>
            > signalWeights;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public DiseaseProfile(

            String disease,

            String category,

            String visualFamily,

            List<String> bodyLocations,

            List<String> morphology,

            int rarityWeight,

            Map<String, Map<String, Integer>>
                    signalWeights
    ) {

        this.disease = disease;

        this.category = category;

        this.visualFamily =
                visualFamily;

        this.bodyLocations =
                bodyLocations;

        this.morphology =
                morphology;

        this.rarityWeight =
                rarityWeight;

        this.signalWeights =
                signalWeights;
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public String getDisease() {
        return disease;
    }

    public String getCategory() {
        return category;
    }

    public String getVisualFamily() {
        return visualFamily;
    }

    public List<String>
    getBodyLocations() {

        return bodyLocations;
    }

    public List<String>
    getMorphology() {

        return morphology;
    }

    public int getRarityWeight() {
        return rarityWeight;
    }

    public Map<
            String,
            Map<String, Integer>
            > getSignalWeights() {

        return signalWeights;
    }
}


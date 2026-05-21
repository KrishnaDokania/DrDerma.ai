package ai.drderma.backend.model;

import java.util.List;
import java.util.Map;

public class DiseaseProfile {

    // =====================================================
    // BASIC
    // =====================================================

    private final String disease;

    // =====================================================
    // REASONING METADATA
    // =====================================================

    private final String category;

    private final String visualFamily;

    private final List<String> morphology;

    private final int rarityWeight;

    // =====================================================
    // SIGNALS
    // =====================================================

    private final Map<String,
            Map<String, Integer>>
            signalWeights;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public DiseaseProfile(

            String disease,

            String category,

            String visualFamily,

            List<String> morphology,

            int rarityWeight,

            Map<String,
                    Map<String, Integer>>
                    signalWeights
    ) {

        this.disease = disease;

        this.category = category;

        this.visualFamily = visualFamily;

        this.morphology = morphology;

        this.rarityWeight = rarityWeight;

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

    public List<String> getMorphology() {
        return morphology;
    }

    public int getRarityWeight() {
        return rarityWeight;
    }

    public Map<String,
            Map<String, Integer>>
    getSignalWeights() {

        return signalWeights;
    }
}
package ai.drderma.backend.model;

import java.util.Map;

public class DiseaseProfile {

    private final String disease;
    private final Map<String, Map<String, Integer>> signalWeights;

    public DiseaseProfile(
        String disease,
        Map<String, Map<String, Integer>> signalWeights
    ) {
        this.disease = disease;
        this.signalWeights = signalWeights;
    }

    public String getDisease() {
        return disease;
    }

    public Map<String, Map<String, Integer>> getSignalWeights() {
        return signalWeights;
    }
}

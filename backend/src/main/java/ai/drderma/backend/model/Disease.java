package ai.drderma.backend.model;

import java.util.Map;

public class Disease {

    private final String id;
    private final String name;
    private final String commonName;
    private final String category;

    // featureKey -> expected value
    private final Map<String, Object> features;

    public Disease(
            String id,
            String name,
            String commonName,
            String category,
            Map<String, Object> features
    ) {
        this.id = id;
        this.name = name;
        this.commonName = commonName;
        this.category = category;
        this.features = features;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getCategory() {
        return category;
    }

    public Map<String, Object> getFeatures() {
        return features;
    }
}

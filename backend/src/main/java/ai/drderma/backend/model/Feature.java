package ai.drderma.backend.model;

import java.util.List;

public class Feature {

    private final String key;          // e.g. "ring_shape"
    private final String question;     // shown to user
    private final FeatureType type;
    private final List<String> options;

    public Feature(
            String key,
            String question,
            FeatureType type,
            List<String> options
    ) {
        this.key = key;
        this.question = question;
        this.type = type;
        this.options = options;
    }

    public String getKey() {
        return key;
    }

    public String getQuestion() {
        return question;
    }

    public FeatureType getType() {
        return type;
    }

    public List<String> getOptions() {
        return options;
    }
}

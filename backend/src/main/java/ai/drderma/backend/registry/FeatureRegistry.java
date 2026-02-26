package ai.drderma.backend.registry;

import ai.drderma.backend.model.Feature;
import ai.drderma.backend.model.FeatureType;

import java.util.*;

public class FeatureRegistry {

    private static final Map<String, Feature> FEATURES = new HashMap<>();

    static {

        FEATURES.put("ring_shape",
                new Feature(
                        "ring_shape",
                        "Is the lesion ring-shaped?",
                        FeatureType.BOOLEAN,
                        null
                )
        );

        FEATURES.put("scaling",
                new Feature(
                        "scaling",
                        "Is there visible scaling or flaking?",
                        FeatureType.BOOLEAN,
                        null
                )
        );

        FEATURES.put("itching",
                new Feature(
                        "itching",
                        "Does the affected area itch?",
                        FeatureType.BOOLEAN,
                        null
                )
        );

        FEATURES.put("painful",
                new Feature(
                        "painful",
                        "Is the lesion painful?",
                        FeatureType.BOOLEAN,
                        null
                )
        );

        FEATURES.put("location",
                new Feature(
                        "location",
                        "Where is the issue mainly located?",
                        FeatureType.ENUM,
                        List.of("Face", "Scalp", "Groin", "Arms", "Legs", "Trunk")
                )
        );

        FEATURES.put("chronic",
                new Feature(
                        "chronic",
                        "Has this condition lasted more than 3 months?",
                        FeatureType.BOOLEAN,
                        null
                )
        );
    }

    public static Collection<Feature> getAll() {
        return FEATURES.values();
    }

    public static Feature getByKey(String key) {
        return FEATURES.get(key);
    }
}

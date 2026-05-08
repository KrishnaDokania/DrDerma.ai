package ai.drderma.backend.questions;

import ai.drderma.backend.model.Feature;
import ai.drderma.backend.model.FeatureType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class FeatureRepository {

    private final Map<String, Feature> features;

    public FeatureRepository() {

        features = new HashMap<>();

        /*
         * UNIVERSAL FEATURES
         */

        features.put(
                "itching",
                new Feature(
                        "itching",
                        "Does the affected area feel itchy?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "redness",
                new Feature(
                        "redness",
                        "Is the affected area red or inflamed?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "pain",
                new Feature(
                        "pain",
                        "Is the affected area painful?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "burning",
                new Feature(
                        "burning",
                        "Do you feel a burning sensation?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "dryness",
                new Feature(
                        "dryness",
                        "Does the skin appear dry or rough?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        /*
         * ACNE RELATED
         */

        features.put(
                "pus_filled_pimples",
                new Feature(
                        "pus_filled_pimples",
                        "Are there pus-filled pimples present?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "blackheads",
                new Feature(
                        "blackheads",
                        "Do you notice blackheads or clogged pores?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "oily_skin",
                new Feature(
                        "oily_skin",
                        "Is your skin unusually oily?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "deep_nodules",
                new Feature(
                        "deep_nodules",
                        "Are there deep painful lumps beneath the skin?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        /*
         * ECZEMA / DERMATITIS
         */

        features.put(
                "scaling",
                new Feature(
                        "scaling",
                        "Is there visible scaling or flaking?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "oozing",
                new Feature(
                        "oozing",
                        "Is there any fluid discharge or oozing?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "crusting",
                new Feature(
                        "crusting",
                        "Has the skin developed crusts or scabs?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "skin_thickening",
                new Feature(
                        "skin_thickening",
                        "Does the skin appear thickened from scratching?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        /*
         * PSORIASIS
         */

        features.put(
                "silvery_scales",
                new Feature(
                        "silvery_scales",
                        "Are the scales thick and silvery in appearance?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "well_defined_border",
                new Feature(
                        "well_defined_border",
                        "Are the edges sharply defined?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "joint_pain",
                new Feature(
                        "joint_pain",
                        "Do you also experience joint pain or stiffness?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        /*
         * FUNGAL INFECTIONS
         */

        features.put(
                "ring_shape",
                new Feature(
                        "ring_shape",
                        "Does the rash form a circular or ring-like pattern?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "central_clearing",
                new Feature(
                        "central_clearing",
                        "Is the center clearer than the outer border?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "sweating_trigger",
                new Feature(
                        "sweating_trigger",
                        "Does sweating worsen the condition?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "hair_loss_patch",
                new Feature(
                        "hair_loss_patch",
                        "Are there patchy areas of hair loss?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        /*
         * VIRAL CONDITIONS
         */

        features.put(
                "blisters",
                new Feature(
                        "blisters",
                        "Are there fluid-filled blisters present?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "grouped_lesions",
                new Feature(
                        "grouped_lesions",
                        "Are the lesions grouped together closely?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "fever",
                new Feature(
                        "fever",
                        "Have you experienced fever recently?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        /*
         * SCABIES
         */

        features.put(
                "night_itching",
                new Feature(
                        "night_itching",
                        "Does the itching become worse at night?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "family_spread",
                new Feature(
                        "family_spread",
                        "Do other family members have similar itching?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        /*
         * ROSACEA / MELASMA
         */

        features.put(
                "facial_flushing",
                new Feature(
                        "facial_flushing",
                        "Does your face flush or become red easily?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "sun_trigger",
                new Feature(
                        "sun_trigger",
                        "Does sunlight worsen the condition?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        features.put(
                "pigmentation",
                new Feature(
                        "pigmentation",
                        "Are there darker patches or pigmentation changes?",
                        FeatureType.BOOLEAN,
                        List.of("yes", "no")
                )
        );

        /*
         * LOCATION
         */

        features.put(
                "location",
                new Feature(
                        "location",
                        "Where is the condition mainly located?",
                        FeatureType.ENUM,
                        List.of(
                                "face",
                                "scalp",
                                "arms",
                                "legs",
                                "hands",
                                "feet",
                                "groin",
                                "trunk",
                                "neck",
                                "multiple areas"
                        )
                )
        );

        /*
         * SEVERITY
         */

        features.put(
                "itch_severity",
                new Feature(
                        "itch_severity",
                        "How severe is the itching?",
                        FeatureType.NUMBER,
                        List.of()
                )
        );
    }

    public Feature getFeature(
            String key
    ) {
        return features.get(key);
    }

    public Set<String> getAllFeatures() {
        return features.keySet();
    }
}
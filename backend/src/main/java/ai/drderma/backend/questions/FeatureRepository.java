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

    private final Map<String, Feature>
            features;

    public FeatureRepository() {

        features = new HashMap<>();

        // =====================================================
        // UNIVERSAL
        // =====================================================

        features.put(
                "itching",
                new Feature(
                        "itching",
                        "Does the affected area feel itchy?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "burning",
                new Feature(
                        "burning",
                        "Do you experience a burning sensation?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "scaling",
                new Feature(
                        "scaling",
                        "Is there visible scaling or flaking?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        // =====================================================
        // TINEA
        // =====================================================

        features.put(
                "ring_shape",
                new Feature(
                        "ring_shape",
                        "Does the rash form a circular or ring-shaped pattern?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "central_clearing",
                new Feature(
                        "central_clearing",
                        "Is the center clearer than the outer border?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "border_elevation",
                new Feature(
                        "border_elevation",
                        "Are the outer edges raised compared to the center?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "sweating",
                new Feature(
                        "sweating",
                        "Does sweating or heat worsen the condition?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        // =====================================================
        // PSORIASIS
        // =====================================================

        features.put(
                "silvery_scale",
                new Feature(
                        "silvery_scale",
                        "Are thick silvery-white scales visible?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "thick_plaques",
                new Feature(
                        "thick_plaques",
                        "Are the lesions thick and raised?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "nail_changes",
                new Feature(
                        "nail_changes",
                        "Are there nail changes like pitting or thickening?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "family_history",
                new Feature(
                        "family_history",
                        "Does anyone in your family have a similar skin condition?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        // =====================================================
        // ECZEMA
        // =====================================================

        features.put(
                "oozing",
                new Feature(
                        "oozing",
                        "Is there fluid discharge or oozing from the skin?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "dry_skin",
                new Feature(
                        "dry_skin",
                        "Does the skin appear very dry or rough?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "allergy_history",
                new Feature(
                        "allergy_history",
                        "Do you have a history of allergies, asthma, or eczema?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        // =====================================================
        // ACNE
        // =====================================================

        features.put(
                "oily_skin",
                new Feature(
                        "oily_skin",
                        "Does your skin appear unusually oily?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "blackheads",
                new Feature(
                        "blackheads",
                        "Are blackheads or clogged pores visible?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "pus_filled_bumps",
                new Feature(
                        "pus_filled_bumps",
                        "Are there pus-filled bumps or pimples?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "painful_lesions",
                new Feature(
                        "painful_lesions",
                        "Are the lesions painful or tender?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        features.put(
                "teenager",
                new Feature(
                        "teenager",
                        "Are you currently in your teenage years?",
                        FeatureType.BOOLEAN,
                        List.of(
                                "yes",
                                "no"
                        )
                )
        );

        // =====================================================
        // LOCATION
        // =====================================================

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
                                "multiple_areas"
                        )
                )
        );

        // =====================================================
        // DURATION
        // =====================================================

        features.put(
                "duration",
                new Feature(
                        "duration",
                        "How long has the condition been present?",
                        FeatureType.ENUM,
                        List.of(
                                "days",
                                "weeks",
                                "months",
                                "years"
                        )
                )
        );
    }

    // =====================================================
    // GET FEATURE
    // =====================================================

    public Feature getFeature(
            String key
    ) {

        return features.get(key);
    }

    // =====================================================
    // GET ALL FEATURES
    // =====================================================

    public Set<String>
    getAllFeatures() {

        return features.keySet();
    }
}
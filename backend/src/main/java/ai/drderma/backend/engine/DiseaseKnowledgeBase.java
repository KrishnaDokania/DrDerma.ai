package ai.drderma.backend.engine;

import ai.drderma.backend.model.DiseaseProfile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DiseaseKnowledgeBase {

    private final Map<String, DiseaseProfile>
            profiles =
            Map.of(

                    // =====================================================
                    // TINEA CORPORIS
                    // =====================================================

                    "tinea_corporis",

                    new DiseaseProfile(

                            "tinea_corporis",

                            "fungal",

                            "ring_scales",

                            List.of(
                                    "ring",
                                    "circular",
                                    "scaly"
                            ),

                            2,

                            Map.of(

                                    "ring_shape",
                                    Map.of(
                                            "yes", 5,
                                            "no", -4
                                    ),

                                    "central_clearing",
                                    Map.of(
                                            "yes", 5,
                                            "no", -4
                                    ),

                                    "border_elevation",
                                    Map.of(
                                            "yes", 4,
                                            "no", -2
                                    ),

                                    "sweating",
                                    Map.of(
                                            "yes", 3,
                                            "no", 0
                                    ),

                                    "itching",
                                    Map.of(
                                            "yes", 3,
                                            "no", -1
                                    ),

                                    "scaling",
                                    Map.of(
                                            "yes", 2,
                                            "no", -2
                                    ),

                                    "location",
                                    Map.of(
                                            "groin", 5,
                                            "arms", 2,
                                            "legs", 2,
                                            "trunk", 2,
                                            "face", -1
                                    ),

                                    "duration",
                                    Map.of(
                                            "days", -2,
                                            "weeks", 3,
                                            "months", 1,
                                            "years", -2
                                    )
                            )
                    ),

                    // =====================================================
                    // PSORIASIS
                    // =====================================================

                    "psoriasis_vulgaris",

                    new DiseaseProfile(

                            "psoriasis_vulgaris",

                            "autoimmune",

                            "thick_plaques",

                            List.of(
                                    "plaque",
                                    "silvery",
                                    "thick"
                            ),

                            3,

                            Map.of(

                                    "silvery_scale",
                                    Map.of(
                                            "yes", 6,
                                            "no", -5
                                    ),

                                    "thick_plaques",
                                    Map.of(
                                            "yes", 5,
                                            "no", -4
                                    ),

                                    "nail_changes",
                                    Map.of(
                                            "yes", 5,
                                            "no", 0
                                    ),

                                    "family_history",
                                    Map.of(
                                            "yes", 4,
                                            "no", 0
                                    ),

                                    "scaling",
                                    Map.of(
                                            "yes", 4,
                                            "no", -3
                                    ),

                                    "itching",
                                    Map.of(
                                            "yes", 1,
                                            "no", 2
                                    ),

                                    "location",
                                    Map.of(
                                            "scalp", 5,
                                            "knees", 5,
                                            "elbows", 5,
                                            "trunk", 2,
                                            "groin", -4
                                    ),

                                    "duration",
                                    Map.of(
                                            "days", -3,
                                            "weeks", 1,
                                            "months", 3,
                                            "years", 5
                                    )
                            )
                    ),

                    // =====================================================
                    // ECZEMA
                    // =====================================================

                    "eczema_atopic_dermatitis",

                    new DiseaseProfile(

                            "eczema_atopic_dermatitis",

                            "inflammatory",

                            "dry_patchy",

                            List.of(
                                    "dry",
                                    "patchy",
                                    "inflamed"
                            ),

                            2,

                            Map.of(

                                    "oozing",
                                    Map.of(
                                            "yes", 5,
                                            "no", -1
                                    ),

                                    "dry_skin",
                                    Map.of(
                                            "yes", 5,
                                            "no", -3
                                    ),

                                    "allergy_history",
                                    Map.of(
                                            "yes", 5,
                                            "no", 0
                                    ),

                                    "itching",
                                    Map.of(
                                            "yes", 5,
                                            "no", -4
                                    ),

                                    "burning",
                                    Map.of(
                                            "yes", 3,
                                            "no", 0
                                    ),

                                    "scaling",
                                    Map.of(
                                            "yes", 2,
                                            "no", 0
                                    ),

                                    "location",
                                    Map.of(
                                            "arms", 4,
                                            "legs", 4,
                                            "neck", 3,
                                            "face", 3,
                                            "groin", -2
                                    ),

                                    "duration",
                                    Map.of(
                                            "days", 0,
                                            "weeks", 3,
                                            "months", 2,
                                            "years", 1
                                    )
                            )
                    ),

                    // =====================================================
                    // ACNE
                    // =====================================================

                    "acne_vulgaris",

                    new DiseaseProfile(

                            "acne_vulgaris",

                            "follicular",

                            "papules_pustules",

                            List.of(
                                    "papule",
                                    "pustule",
                                    "comedone"
                            ),

                            1,

                            Map.of(

                                    "blackheads",
                                    Map.of(
                                            "yes", 6,
                                            "no", -5
                                    ),

                                    "pus_filled_bumps",
                                    Map.of(
                                            "yes", 5,
                                            "no", -3
                                    ),

                                    "oily_skin",
                                    Map.of(
                                            "yes", 4,
                                            "no", -1
                                    ),

                                    "painful_lesions",
                                    Map.of(
                                            "yes", 3,
                                            "no", 0
                                    ),

                                    "teenager",
                                    Map.of(
                                            "yes", 4,
                                            "no", -1
                                    ),

                                    "itching",
                                    Map.of(
                                            "yes", 0,
                                            "no", 1
                                    ),

                                    "location",
                                    Map.of(
                                            "face", 5,
                                            "neck", 3,
                                            "trunk", 2,
                                            "arms", -2,
                                            "legs", -3
                                    ),

                                    "duration",
                                    Map.of(
                                            "days", 0,
                                            "weeks", 2,
                                            "months", 4,
                                            "years", 2
                                    )
                            )
                    )
            );

    // =====================================================
    // GET PROFILE
    // =====================================================

    public DiseaseProfile get(
            String disease
    ) {

        return profiles.get(disease);
    }
}
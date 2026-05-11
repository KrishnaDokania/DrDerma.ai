package ai.drderma.backend.engine;

import ai.drderma.backend.model.DiseaseProfile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DiseaseKnowledgeBase {

    private final Map<String, DiseaseProfile>
            profiles = Map.of(

            // =========================================
            // TINEA CORPORIS
            // =========================================

            "tinea_corporis",

            new DiseaseProfile(

                    "tinea_corporis",

                    "fungal",

                    "annular_fungal",

                    List.of(
                            "groin",
                            "arms",
                            "legs",
                            "trunk"
                    ),

                    List.of(
                            "ring",
                            "scaly",
                            "circular",
                            "central_clearing"
                    ),

                    5,

                    Map.of(

                            "ring_shape",
                            Map.of(
                                    "yes", 5,
                                    "no", -4
                            ),

                            "central_clearing",
                            Map.of(
                                    "yes", 5,
                                    "no", -3
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

                            "location",
                            Map.of(
                                    "groin", 4,
                                    "arms", 3,
                                    "legs", 3,
                                    "face", -2
                            )
                    )
            ),

            // =========================================
            // PSORIASIS
            // =========================================

            "psoriasis_vulgaris",

            new DiseaseProfile(

                    "psoriasis_vulgaris",

                    "autoimmune",

                    "scaly_plaque",

                    List.of(
                            "scalp",
                            "elbows",
                            "knees",
                            "trunk"
                    ),

                    List.of(
                            "plaque",
                            "silvery",
                            "thick",
                            "scaly"
                    ),

                    5,

                    Map.of(

                            "silvery_scale",
                            Map.of(
                                    "yes", 5,
                                    "no", -4
                            ),

                            "thick_plaques",
                            Map.of(
                                    "yes", 5,
                                    "no", -3
                            ),

                            "nail_changes",
                            Map.of(
                                    "yes", 4,
                                    "no", 0
                            ),

                            "family_history",
                            Map.of(
                                    "yes", 3,
                                    "no", 0
                            ),

                            "scaling",
                            Map.of(
                                    "yes", 4,
                                    "no", -3
                            ),

                            "location",
                            Map.of(
                                    "scalp", 4,
                                    "elbows", 4,
                                    "knees", 4
                            )
                    )
            ),

            // =========================================
            // ECZEMA
            // =========================================

            "eczema_atopic_dermatitis",

            new DiseaseProfile(

                    "eczema_atopic_dermatitis",

                    "inflammatory",

                    "inflammatory_patch",

                    List.of(
                            "face",
                            "arms",
                            "legs",
                            "neck"
                    ),

                    List.of(
                            "itchy",
                            "inflamed",
                            "patchy",
                            "dry"
                    ),

                    4,

                    Map.of(

                            "itching",
                            Map.of(
                                    "yes", 5,
                                    "no", -4
                            ),

                            "oozing",
                            Map.of(
                                    "yes", 5,
                                    "no", -2
                            ),

                            "burning",
                            Map.of(
                                    "yes", 3,
                                    "no", 0
                            ),

                            "dry_skin",
                            Map.of(
                                    "yes", 4,
                                    "no", -1
                            ),

                            "allergy_history",
                            Map.of(
                                    "yes", 4,
                                    "no", 0
                            ),

                            "location",
                            Map.of(
                                    "face", 3,
                                    "arms", 3,
                                    "legs", 2
                            )
                    )
            ),

            // =========================================
            // ACNE
            // =========================================

            "acne_vulgaris",

            new DiseaseProfile(

                    "acne_vulgaris",

                    "acneiform",

                    "papulopustular",

                    List.of(
                            "face",
                            "back",
                            "chest"
                    ),

                    List.of(
                            "papule",
                            "pustule",
                            "comedone",
                            "oily"
                    ),

                    4,

                    Map.of(

                            "oily_skin",
                            Map.of(
                                    "yes", 5,
                                    "no", -3
                            ),

                            "blackheads",
                            Map.of(
                                    "yes", 5,
                                    "no", -4
                            ),

                            "pus_filled_bumps",
                            Map.of(
                                    "yes", 4,
                                    "no", -2
                            ),

                            "painful_lesions",
                            Map.of(
                                    "yes", 3,
                                    "no", 0
                            ),

                            "teenager",
                            Map.of(
                                    "yes", 3,
                                    "no", -1
                            ),

                            "location",
                            Map.of(
                                    "face", 5,
                                    "back", 4,
                                    "chest", 4
                            )
                    )
            )
    );

    public DiseaseProfile get(
            String disease
    ) {

        return profiles.get(disease);
    }

    public Map<String, DiseaseProfile>
    getProfiles() {

        return profiles;
    }
}
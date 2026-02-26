package ai.drderma.backend.engine;

import ai.drderma.backend.model.DiseaseProfile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DiseaseKnowledgeBase {

    private final Map<String, DiseaseProfile> profiles = Map.of(

        // ───────────────────────── TINEA ─────────────────────────
        "tinea_corporis",
        new DiseaseProfile(
            "tinea_corporis",
            Map.of(
                "itching", Map.of(
                    "yes", 3,
                    "no", -1
                ),
                "scaling", Map.of(
                    "yes", 2,
                    "no", -2
                ),
                "ring_shape", Map.of(
                    "yes", 4,
                    "no", -3
                ),
                "central_clearing", Map.of(
                    "yes", 4,
                    "no", -2
                ),
                "sweating", Map.of(
                    "yes", 3,
                    "no", 0
                ),
                "duration", Map.of(
                    "weeks", 2,
                    "months", 1,
                    "years", -2
                ),
                "location", Map.of(
                    "groin", 3,
                    "arms", 2,
                    "legs", 2,
                    "face", -2,
                    "scalp", -3
                )
            )
        ),

        // ─────────────────────── PSORIASIS ───────────────────────
        "psoriasis_vulgaris",
        new DiseaseProfile(
            "psoriasis_vulgaris",
            Map.of(
                "itching", Map.of(
                    "yes", 1,
                    "no", 2
                ),
                "scaling", Map.of(
                    "yes", 4,
                    "no", -3
                ),
                "thick_plaques", Map.of(
                    "yes", 4,
                    "no", -2
                ),
                "nail_changes", Map.of(
                    "yes", 4,
                    "no", 0
                ),
                "family_history", Map.of(
                    "yes", 3,
                    "no", 0
                ),
                "duration", Map.of(
                    "months", 2,
                    "years", 3,
                    "weeks", -1
                ),
                "location", Map.of(
                    "scalp", 3,
                    "elbows", 3,
                    "knees", 3,
                    "groin", -3,
                    "face", -1
                )
            )
        ),

        // ───────────────────────── ECZEMA ─────────────────────────
        "eczema_atopic_dermatitis",
        new DiseaseProfile(
            "eczema_atopic_dermatitis",
            Map.of(
                "itching", Map.of(
                    "yes", 4,
                    "no", -3
                ),
                "scaling", Map.of(
                    "yes", 2,
                    "no", 0
                ),
                "oozing", Map.of(
                    "yes", 4,
                    "no", -1
                ),
                "burning", Map.of(
                    "yes", 3,
                    "no", 0
                ),
                "allergy_history", Map.of(
                    "yes", 3,
                    "no", 0
                ),
                "duration", Map.of(
                    "weeks", 2,
                    "months", 1,
                    "years", 1
                ),
                "location", Map.of(
                    "face", 2,
                    "arms", 2,
                    "legs", 1,
                    "groin", -2,
                    "scalp", -1
                )
            )
        )
    );

    public DiseaseProfile get(String disease) {
        return profiles.get(disease);
    }
}

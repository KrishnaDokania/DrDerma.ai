package ai.drderma.backend.registry;

import ai.drderma.backend.model.Disease;

import java.util.*;

public class DiseaseRegistry {

    private static final Map<String, Disease> DISEASES = new HashMap<>();

    static {

        // --- FUNGAL ---
        DISEASES.put("tinea_corporis",
                new Disease(
                        "tinea_corporis",
                        "Tinea corporis",
                        "Ringworm",
                        "fungal",
                        Map.of(
                                "ring_shape", true,
                                "scaling", true,
                                "itching", true
                        )
                )
        );

        // --- INFLAMMATORY ---
        DISEASES.put("psoriasis_vulgaris",
                new Disease(
                        "psoriasis_vulgaris",
                        "Psoriasis vulgaris",
                        "Psoriasis",
                        "inflammatory",
                        Map.of(
                                "scaling", true,
                                "chronic", true,
                                "ring_shape", false
                        )
                )
        );

        DISEASES.put("atopic_dermatitis",
                new Disease(
                        "atopic_dermatitis",
                        "Atopic dermatitis",
                        "Eczema",
                        "inflammatory",
                        Map.of(
                                "itching", true,
                                "chronic", true
                        )
                )
        );

        // --- PIGMENTARY ---
        DISEASES.put("vitiligo",
                new Disease(
                        "vitiligo",
                        "Vitiligo",
                        "Vitiligo",
                        "pigmentary",
                        Map.of(
                                "scaling", false,
                                "itching", false
                        )
                )
        );
    }

    public static Collection<Disease> getAll() {
        return DISEASES.values();
    }

    public static Disease getById(String id) {
        return DISEASES.get(id);
    }
}

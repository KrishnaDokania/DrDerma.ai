package ai.drderma.backend.service;

import ai.drderma.backend.model.ImageCandidate;
import ai.drderma.backend.model.VisualTraits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisualHeuristicService {

    public List<ImageCandidate>
    generateCandidates(
            String imageName
    ) {

        imageName =
                imageName.toLowerCase();

        List<ImageCandidate>
                candidates =
                new ArrayList<>();

        // =====================================
        // CHICKENPOX
        // =====================================

        if (
                imageName.contains("chicken")
                        ||
                        imageName.contains("pox")
                        ||
                        imageName.contains("vesicle")
        ) {

            candidates.add(
                    build(
                            "varicella_chickenpox",
                            92,
                            List.of(
                                    "vesicle",
                                    "fluid_filled"
                            )
                    )
            );

            candidates.add(
                    build(
                            "eczema_atopic_dermatitis",
                            45,
                            List.of(
                                    "patchy"
                            )
                    )
            );

            return candidates;
        }

        // =====================================
        // ACNE
        // =====================================

        if (
                imageName.contains("acne")
                        ||
                        imageName.contains("pimple")
        ) {

            candidates.add(
                    build(
                            "acne_vulgaris",
                            90,
                            List.of(
                                    "papule",
                                    "pustule"
                            )
                    )
            );

            return candidates;
        }

        // =====================================
        // FUNGAL
        // =====================================

        if (
                imageName.contains("ring")
                        ||
                        imageName.contains("fungal")
        ) {

            candidates.add(
                    build(
                            "tinea_corporis",
                            91,
                            List.of(
                                    "ring",
                                    "scaly"
                            )
                    )
            );

            return candidates;
        }

        // =====================================
        // DEFAULT
        // =====================================

        candidates.add(
                build(
                        "eczema_atopic_dermatitis",
                        70,
                        List.of(
                                "patchy"
                        )
                )
        );

        candidates.add(
                build(
                        "psoriasis_vulgaris",
                        68,
                        List.of(
                                "plaque"
                        )
                )
        );

        return candidates;
    }

    // =========================================
    // BUILD
    // =========================================

    private ImageCandidate build(

            String disease,

            double similarity,

            List<String> morphology
    ) {

        ImageCandidate candidate =
                new ImageCandidate(
                        disease,
                        similarity
                );

        VisualTraits traits =
                new VisualTraits();

        traits.setMorphology(
                morphology
        );

        candidate.setTraits(
                traits
        );

        return candidate;
    }
}
package ai.drderma.backend.controller;

import ai.drderma.backend.model.*;
import ai.drderma.backend.service.TriageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ImageAnalysisController {

    @Autowired
    private TriageService triageService;

    // =====================================================
    // START ANALYSIS
    // =====================================================

    @PostMapping("/analyze")
    public Map<String, Object> analyze(

            @RequestParam("image")
            MultipartFile image
    ) {

        // =============================================
        // MOCK EMBEDDING OUTPUT
        // =============================================

        // Replace later with real vector search

        Map<String, Double> similarities =
                new HashMap<>();

        similarities.put(
                "psoriasis_vulgaris",
                82.0
        );

        similarities.put(
                "eczema_atopic_dermatitis",
                68.0
        );

        similarities.put(
                "tinea_corporis",
                52.0
        );

        similarities.put(
                "acne_vulgaris",
                33.0
        );

        // =============================================
        // IMAGE CANDIDATES
        // =============================================

        List<ImageCandidate>
                imageCandidates =
                new ArrayList<>();

        for (
                Map.Entry<String, Double>
                        entry :
                similarities.entrySet()
        ) {

            String disease =
                    entry.getKey();

            double similarity =
                    entry.getValue();

            ImageCandidate candidate =
                    new ImageCandidate(

                            disease,

                            similarity
                    );

            // =====================================
            // VISUAL TRAITS
            // =====================================

            VisualTraits traits =
                    new VisualTraits();

            // PSORIASIS

            if (
                    disease.equals(
                            "psoriasis_vulgaris"
                    )
            ) {

                traits.setMorphology(
                        List.of(
                                "plaque",
                                "scaly",
                                "silvery"
                        )
                );

                traits.setTextures(
                        List.of(
                                "dry"
                        )
                );

                traits.setColors(
                        List.of(
                                "red"
                        )
                );
            }

            // ECZEMA

            else if (
                    disease.equals(
                            "eczema_atopic_dermatitis"
                    )
            ) {

                traits.setMorphology(
                        List.of(
                                "patchy",
                                "inflamed",
                                "dry"
                        )
                );

                traits.setTextures(
                        List.of(
                                "rough"
                        )
                );

                traits.setColors(
                        List.of(
                                "pink"
                        )
                );
            }

            // TINEA

            else if (
                    disease.equals(
                            "tinea_corporis"
                    )
            ) {

                traits.setMorphology(
                        List.of(
                                "ring",
                                "scaly",
                                "circular"
                        )
                );

                traits.setTextures(
                        List.of(
                                "raised_border"
                        )
                );

                traits.setColors(
                        List.of(
                                "red"
                        )
                );
            }

            // ACNE

            else if (
                    disease.equals(
                            "acne_vulgaris"
                    )
            ) {

                traits.setMorphology(
                        List.of(
                                "papule",
                                "pustule",
                                "comedone"
                        )
                );

                traits.setTextures(
                        List.of(
                                "oily"
                        )
                );

                traits.setColors(
                        List.of(
                                "red"
                        )
                );
            }

            candidate.setTraits(
                    traits
            );

            imageCandidates.add(
                    candidate
            );
        }

        // =============================================
        // START TRIAGE SESSION
        // =============================================

        return triageService.start(
                imageCandidates
        );
    }

    // =====================================================
    // ANSWER QUESTION
    // =====================================================

    @PostMapping("/answer")
    public Map<String, Object> answer(

            @RequestParam String sessionId,

            @RequestParam String signal,

            @RequestParam String answer
    ) {

        return triageService.answer(

                sessionId,

                signal,

                answer
        );
    }
}
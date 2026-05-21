package ai.drderma.backend.controller;

import ai.drderma.backend.model.ImageCandidate;
import ai.drderma.backend.service.TriageService;
import ai.drderma.backend.service.VisualHeuristicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ImageAnalysisController {

    @Autowired
    private TriageService triageService;

    @Autowired
    private VisualHeuristicService
            visualHeuristicService;

    // =====================================================
    // ANALYZE IMAGE
    // =====================================================

    @PostMapping("/analyze")
    public Map<String, Object> analyze(

            @RequestParam("image")
            MultipartFile image
    ) {

        // =============================================
        // IMAGE-DRIVEN CANDIDATES
        // =============================================

        List<ImageCandidate>
                imageCandidates =

                visualHeuristicService
                        .generateCandidates(

                                image.getOriginalFilename()
                        );

        // =============================================
        // START TRIAGE
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
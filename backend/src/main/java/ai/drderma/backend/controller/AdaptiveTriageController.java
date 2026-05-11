package ai.drderma.backend.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import ai.drderma.backend.model.ImageCandidate;
import ai.drderma.backend.model.AnswerRequest;
import ai.drderma.backend.service.TriageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/triage")
@CrossOrigin(origins = "http://localhost:5173")
public class AdaptiveTriageController {

    private final TriageService triageService;

    public AdaptiveTriageController(TriageService triageService) {
        this.triageService = triageService;
    }

    @PostMapping("/start")
    public Map<String, Object> start(
            @RequestBody List<ImageCandidate> candidates
    ) {
        return triageService.start(candidates);
    }

     @PostMapping("/answer")
public Map<String, Object> answer(
        @RequestBody AnswerRequest request
) {

    return triageService.answer(
            request.getSessionId(),
            request.getSignal(),
            request.getAnswer()
    );
}
}

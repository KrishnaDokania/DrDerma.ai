package ai.drderma.backend.controller;

import ai.drderma.backend.model.CandidateState;
import ai.drderma.backend.engine.DiseaseKnowledgeBase;
import ai.drderma.backend.image.EmbeddingStore;
import ai.drderma.backend.image.ImageSimilarityEngine;
import ai.drderma.backend.image.MlClient;
import ai.drderma.backend.model.DiseaseProfile;
import ai.drderma.backend.questions.QuestionEngine;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/image")
public class ImageAnalysisController {

    private static final double CONFIDENCE_THRESHOLD = 0.25;
    private static final int MAX_QUESTIONS = 4;
    private final QuestionEngine questionEngine;
    private final MlClient mlClient;
    private final EmbeddingStore embeddingStore;
    private final DiseaseKnowledgeBase knowledgeBase;

    public ImageAnalysisController(
        MlClient mlClient,
        EmbeddingStore embeddingStore,
        DiseaseKnowledgeBase knowledgeBase,
        QuestionEngine questionEngine
) {
    this.mlClient = mlClient;
    this.embeddingStore = embeddingStore;
    this.knowledgeBase = knowledgeBase;
    this.questionEngine = questionEngine;
}
    @PostMapping("/validate")
public Map<String, Object> validateImage(
        @RequestParam("image") MultipartFile image
) {
    // 🔥 TEMP LOGIC (replace with your skin model)
    boolean isSkin = true;
    System.out.println("VALIDATION RESULT: " + isSkin);

    return Map.of(
            "isSkin", isSkin
    );
}

    // =========================================================
    // IMAGE ANALYSIS
    // =========================================================
 @PostMapping("/analyze")
public Map<String, Object> analyze(@RequestParam("image") MultipartFile image) {

    try {

        // 🔥 STEP 1: BASIC VALIDATION
        if (image == null || image.isEmpty()) {
            return Map.of(
                    "isSkin", false,
                    "stage", "invalid",
                    "reason", "Empty image"
            );
        }

        // 🔥 STEP 2: YOUR SKIN CHECK (TEMP)
        boolean isSkin = true; // replace with real model

        if (!isSkin) {
            return Map.of(
                    "isSkin", false,
                    "stage", "invalid",
                    "reason", "No skin structure detected"
            );
        }

        // 🔥 STEP 3: EMBEDDING (THIS WAS CRASHING)
        double[] queryVector = mlClient.embed(image);

        Map<String, Double> similarities =
                ImageSimilarityEngine.topDiseaseSimilarities(
                        queryVector,
                        embeddingStore.getVectors(),
                        embeddingStore.getLabels(),
                        3
                );

        List<CandidateState> candidates = similarities.entrySet()
                .stream()
                .map(entry -> new CandidateState(
                        entry.getKey(),
                        entry.getValue()
                ))
                .sorted((a, b) ->
                        Double.compare(b.getSimilarityScore(), a.getSimilarityScore()))
                .toList();

      String nextQuestion = questionEngine.selectNextQuestion(
        candidates,
        knowledgeBase,
        new HashSet<>()
);

        Map<String, Object> response = Map.of(
                "isSkin", true,
                "stage", "questioning",
                "candidates", candidates,
                "nextQuestion", nextQuestion
        );

        return response;

    } catch (Exception e) {

        e.printStackTrace();

        // 🔥 CRITICAL: NEVER RETURN 400
        return Map.of(
                "isSkin", false,
                "stage", "invalid",
                "reason", "Image processing failed"
        );
    }
}
    // =========================================================
    // ANSWER PROCESSING (FIXED)
    // =========================================================
    @PostMapping("/answer")
    public Map<String, Object> processAnswer(
            @RequestBody Map<String, Object> request
    ) {

        System.out.println("REQUEST PAYLOAD: " + request);

        String question = (String) request.get("question");
        String answer = (String) request.get("answer");

        List<String> askedQuestions =
                (List<String>) request.get("askedQuestions");

        if (askedQuestions == null) {
            askedQuestions = new ArrayList<>();
        }

        List<Map<String, Object>> incomingCandidates =
                (List<Map<String, Object>>) request.get("candidates");

        // 🔴 SAFETY CHECK
        if (incomingCandidates == null || incomingCandidates.isEmpty()) {
            return Map.of(
                    "stage", "error",
                    "message", "No candidates received"
            );
        }

        // =====================================================
        // SAFE MAPPING (FIXED)
        // =====================================================
        List<CandidateState> candidates = new ArrayList<>();

        for (Map<String, Object> c : incomingCandidates) {

            if (c == null) continue;

            String disease = (String) c.get("disease");
            Number similarityNum = (Number) c.get("similarity");

            if (disease == null || similarityNum == null) continue;

            CandidateState cs = new CandidateState(
                    disease,
                    similarityNum.doubleValue()
            );

            if (c.containsKey("questionScore") && c.get("questionScore") != null) {
                cs.addQuestionScore(
                        ((Number) c.get("questionScore")).doubleValue()
                );
            }

            if (c.containsKey("questionImpact") && c.get("questionImpact") != null) {

                Map<String, Object> rawImpact =
                        (Map<String, Object>) c.get("questionImpact");

                Map<String, Integer> safeImpact = new HashMap<>();

                for (Map.Entry<String, Object> entry : rawImpact.entrySet()) {
                    if (entry.getValue() instanceof Number) {
                        safeImpact.put(
                                entry.getKey(),
                                ((Number) entry.getValue()).intValue()
                        );
                    }
                }

                cs.setQuestionImpact(safeImpact);
            }

            candidates.add(cs);
        }

        // 🔴 FINAL SAFETY CHECK
        if (candidates.isEmpty()) {
            return Map.of(
                    "stage", "error",
                    "message", "Candidates parsing failed"
            );
        }

        // =====================================================
        // APPLY SCORING
        // =====================================================
        for (CandidateState c : candidates) {

            DiseaseProfile profile = knowledgeBase.get(c.getDisease());
            if (profile == null) continue;

            Map<String, Map<String, Integer>> signals =
                    profile.getSignalWeights();

            if (signals.containsKey(question)) {

                Map<String, Integer> answerMap =
                        signals.get(question);

                Integer delta = answerMap.get(answer);

                if (delta != null) {
                    c.addQuestionScore(delta);
                    c.recordImpact(question + " = " + answer, delta);
                }
            }
        }

        candidates = candidates.stream()
                .sorted((a, b) ->
                        Double.compare(b.getFinalScore(), a.getFinalScore()))
                .toList();

        double gap = computeFinalGap(candidates);
        double confidence = computeConfidence(candidates);
        System.out.println("Candidates after scoring: " + candidates.size());
        // 🔥 CRITICAL SAFETY CHECK (FINAL FIX)
if (candidates == null || candidates.isEmpty()) {
    return Map.of(
            "stage", "error",
            "message", "No valid candidates after processing",
            "candidates", new ArrayList<>(),
            "nextQuestion", null
    );
}

        String stage;

        if (candidates.get(0).getFinalScore() < 0.2) {
            stage = "low_confidence";
        }
        else if (gap >= CONFIDENCE_THRESHOLD && askedQuestions.size() >= 1) {
            stage = "final_result";
        }
        else if (askedQuestions.size() >= MAX_QUESTIONS) {
            stage = "uncertain";
        }
        else {
            stage = "questioning";
        }

        String nextQuestion = null;

        if ("questioning".equals(stage)) {

            Set<String> askedSet = new HashSet<>(askedQuestions);
            askedSet.add(question);

           nextQuestion = questionEngine.selectNextQuestion(
                candidates,
                knowledgeBase,
                askedSet
);
        }

        Map<String, Object> response =
                buildResponse(stage, gap, confidence, candidates, nextQuestion);

        if ("final_result".equals(stage)) {
            CandidateState top = candidates.get(0);
            response.put("topDisease", top.getDisease());
            response.put("explanation", buildExplanation(top));
        }

        return response;
    }

    // =========================================================
    // UTIL METHODS (UNCHANGED)
    // =========================================================
    private double computeSimilarityGap(List<CandidateState> candidates) {
        if (candidates.size() < 2) return 0.0;
        return candidates.get(0).getSimilarityScore()
                - candidates.get(1).getSimilarityScore();
    }

    private double computeFinalGap(List<CandidateState> candidates) {
        if (candidates.size() < 2) return 0.0;
        return candidates.get(0).getFinalScore()
                - candidates.get(1).getFinalScore();
    }

    private double computeConfidence(List<CandidateState> candidates) {
        if (candidates.size() < 2) return 0.0;

        double top1 = candidates.get(0).getFinalScore();
        double top2 = candidates.get(1).getFinalScore();

        return (top1 - top2) / (Math.abs(top1) + 0.01);
    }

    private Map<String, Object> buildExplanation(
            CandidateState topCandidate
    ) {

        Map<String, Object> explanation = new HashMap<>();

        explanation.put("imageEvidence",
                "Image similarity score: "
                        + topCandidate.getSimilarityScore());

        explanation.put("questionEvidence",
                topCandidate.getQuestionImpact());

        return explanation;
    }

    private Map<String, Object> buildResponse(
            String stage,
            double gap,
            double confidence,
            List<CandidateState> candidates,
            String nextQuestion
    ) {

        List<Map<String, Object>> responseCandidates =
                candidates.stream()
                        .map(c -> {
                            Map<String, Object> m = new HashMap<>();
                            m.put("disease", c.getDisease());
                            m.put("similarity", c.getSimilarityScore());
                            m.put("questionScore", c.getQuestionScore());
                            m.put("finalScore", c.getFinalScore());
                            m.put("questionImpact", c.getQuestionImpact());
                            return m;
                        })
                        .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("stage", stage);
        response.put("gap", gap);
        response.put("confidence", confidence);
        response.put("candidates", responseCandidates);
        response.put("nextQuestion", nextQuestion);

        return response;
    }
}
package ai.drderma.backend.controller;

import ai.drderma.backend.model.CandidateState;
import ai.drderma.backend.engine.*;
import ai.drderma.backend.model.DiseaseProfile;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/image")
public class ImageAnalysisController {

    private static final double CONFIDENCE_THRESHOLD = 0.25;
    private static final int MAX_QUESTIONS = 4;

    private final MlClient mlClient;
    private final EmbeddingStore embeddingStore;
    private final DiseaseKnowledgeBase knowledgeBase;

    public ImageAnalysisController(
            MlClient mlClient,
            EmbeddingStore embeddingStore,
            DiseaseKnowledgeBase knowledgeBase
    ) {
        this.mlClient = mlClient;
        this.embeddingStore = embeddingStore;
        this.knowledgeBase = knowledgeBase;
    }

    // =========================================================
    // IMAGE ANALYSIS
    // =========================================================
    @PostMapping("/analyze")
    public Map<String, Object> analyze(@RequestParam("image") MultipartFile image) {

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

        double gap = computeSimilarityGap(candidates);

        String nextQuestion = QuestionEngine.selectNextQuestion(
                candidates,
                knowledgeBase,
                new HashSet<>()
        );

        return buildResponse(
                "questioning",
                gap,
                0.0,
                candidates,
                nextQuestion
        );
    }

    // =========================================================
    // ANSWER PROCESSING
    // =========================================================
    @PostMapping("/answer")
    public Map<String, Object> processAnswer(
            @RequestBody Map<String, Object> request
    ) {

        String question = (String) request.get("question");
        String answer = (String) request.get("answer");

        List<String> askedQuestions =
                (List<String>) request.get("askedQuestions");

        if (askedQuestions == null) {
            askedQuestions = new ArrayList<>();
        }

        List<Map<String, Object>> incomingCandidates =
                (List<Map<String, Object>>) request.get("candidates");

        List<CandidateState> candidates = incomingCandidates.stream()
                .map(c -> {
                    CandidateState cs = new CandidateState(
                            (String) c.get("disease"),
                            ((Number) c.get("similarity")).doubleValue()
                    );

                    if (c.containsKey("questionScore")) {
                        cs.addQuestionScore(
                                ((Number) c.get("questionScore")).doubleValue()
                        );
                    if (c.containsKey("questionImpact")) {

    Map<String, Object> rawImpact =
            (Map<String, Object>) c.get("questionImpact");

    Map<String, Integer> safeImpact = new HashMap<>();

    for (Map.Entry<String, Object> entry : rawImpact.entrySet()) {
        safeImpact.put(
                entry.getKey(),
                ((Number) entry.getValue()).intValue()
        );
    }

    cs.setQuestionImpact(safeImpact);
}
}

                    return cs;
                })
                .toList();

        // 🔥 Apply answer scoring + record impact
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

            nextQuestion = QuestionEngine.selectNextQuestion(
                    candidates,
                    knowledgeBase,
                    askedSet
            );
        }

        Map<String, Object> response =
                buildResponse(stage, gap, confidence, candidates, nextQuestion);

        // 🔥 Inject explanation if final
        if ("final_result".equals(stage)) {
            CandidateState top = candidates.get(0);
            response.put("topDisease", top.getDisease());
            response.put("explanation", buildExplanation(top));
        }

        return response;
    }

    // =========================================================
    // UTIL METHODS
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

        double similarityContribution =
                0.6 * topCandidate.getSimilarityScore();

        double questionContribution =
                0.4 * (topCandidate.getQuestionScore() / 10.0);

        explanation.put("imageEvidence",
                "Image similarity score: "
                        + topCandidate.getSimilarityScore());

        explanation.put("questionEvidence",
                topCandidate.getQuestionImpact());

        Map<String, Object> breakdown = new HashMap<>();
        breakdown.put("similarityContribution",
                similarityContribution);
        breakdown.put("questionContribution",
                questionContribution);

        explanation.put("scoreBreakdown", breakdown);

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
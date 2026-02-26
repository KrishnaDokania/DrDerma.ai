package ai.drderma.backend.engine;

import ai.drderma.backend.model.CandidateState;
import ai.drderma.backend.model.DiseaseProfile;

import java.util.*;

public class QuestionEngine {

    public static String selectNextQuestion(
            List<CandidateState> candidates,
            DiseaseKnowledgeBase knowledgeBase,
            Set<String> askedQuestions
    ) {

        String bestQuestion = null;
        double bestExpectedGap = -1;

        for (CandidateState c : candidates) {

            DiseaseProfile profile =
                    knowledgeBase.get(c.getDisease());

            if (profile == null) continue;

            for (String question :
                    profile.getSignalWeights().keySet()) {

                if (askedQuestions.contains(question)) continue;

                double expectedGap =
                        simulateExpectedGap(
                                question,
                                candidates,
                                knowledgeBase
                        );

                if (expectedGap > bestExpectedGap) {
                    bestExpectedGap = expectedGap;
                    bestQuestion = question;
                }
            }
        }

        return bestQuestion;
    }

    private static double simulateExpectedGap(
            String question,
            List<CandidateState> candidates,
            DiseaseKnowledgeBase knowledgeBase
    ) {

        // Collect all possible answer options
        Set<String> possibleAnswers = new HashSet<>();

        for (CandidateState c : candidates) {

            DiseaseProfile profile =
                    knowledgeBase.get(c.getDisease());

            if (profile == null) continue;

            Map<String, Map<String, Integer>> signals =
                    profile.getSignalWeights();

            if (signals.containsKey(question)) {
                possibleAnswers.addAll(
                        signals.get(question).keySet()
                );
            }
        }

        double totalGap = 0.0;

        for (String answer : possibleAnswers) {

            List<CandidateState> simulated =
                    deepCopyCandidates(candidates);

            // Apply simulated answer
            for (CandidateState c : simulated) {

                DiseaseProfile profile =
                        knowledgeBase.get(c.getDisease());

                if (profile == null) continue;

                Map<String, Map<String, Integer>> signals =
                        profile.getSignalWeights();

                if (signals.containsKey(question)) {

                    Map<String, Integer> answerMap =
                            signals.get(question);

                    Integer delta = answerMap.get(answer);

                    if (delta != null) {
                        c.addQuestionScore(delta);
                    }
                }
            }

            simulated.sort((a, b) ->
                    Double.compare(
                            b.getFinalScore(),
                            a.getFinalScore()
                    )
            );

            if (simulated.size() >= 2) {
                double gap =
                        simulated.get(0).getFinalScore()
                                - simulated.get(1).getFinalScore();

                totalGap += gap;
            }
        }

        // Expected gap across possible answers
        return possibleAnswers.isEmpty()
                ? 0
                : totalGap / possibleAnswers.size();
    }

    private static List<CandidateState> deepCopyCandidates(
            List<CandidateState> original
    ) {

        List<CandidateState> copy = new ArrayList<>();

        for (CandidateState c : original) {
            CandidateState newC =
                    new CandidateState(
                            c.getDisease(),
                            c.getSimilarityScore()
                    );

            newC.addQuestionScore(
                    c.getQuestionScore()
            );

            copy.add(newC);
        }

        return copy;
    }
}
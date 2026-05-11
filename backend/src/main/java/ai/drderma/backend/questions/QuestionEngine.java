package ai.drderma.backend.questions;

import ai.drderma.backend.engine.DiseaseKnowledgeBase;
import ai.drderma.backend.model.CandidateState;
import ai.drderma.backend.model.DiseaseProfile;
import ai.drderma.backend.model.VisualTraits;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class QuestionEngine {

    public static String selectNextQuestion(

            List<CandidateState> candidates,

            DiseaseKnowledgeBase knowledgeBase,

            Set<String> askedQuestions
    ) {

        if (
                candidates == null
                        || candidates.isEmpty()
        ) {

            return null;
        }

        List<CandidateState> ranked =
                new ArrayList<>(candidates);

        ranked.sort((a, b) ->

                Double.compare(
                        b.getFinalScore(),
                        a.getFinalScore()
                )
        );

        List<CandidateState> topCandidates =
                ranked.stream()
                        .limit(5)
                        .collect(Collectors.toList());

        String dominantCategory =
                dominantCategory(
                        topCandidates,
                        knowledgeBase
                );

        String dominantVisualFamily =
                dominantVisualFamily(
                        topCandidates,
                        knowledgeBase
                );

        Set<String> dominantTraits =
                extractDominantTraits(
                        topCandidates
                );

        Map<String, Integer>
        questionFrequency =
        new HashMap<>();

for (
        CandidateState candidate :
        topCandidates
) {

    DiseaseProfile profile =
            knowledgeBase.get(
                    candidate.getDisease()
            );

    if (profile == null) {
        continue;
    }

    for (
            String question :
            profile.getSignalWeights()
                    .keySet()
    ) {

        if (
                askedQuestions.contains(
                        question
                )
        ) {

            continue;
        }

        questionFrequency.merge(

                question,

                1,

                Integer::sum
        );
    }
}

Set<String> possibleQuestions =

        questionFrequency

                .entrySet()

                .stream()

                .sorted(
                        (a, b) -> Integer.compare(
                                b.getValue(),
                                a.getValue()
                        )
                )

                .map(
                        Map.Entry::getKey
                )

                .collect(
                        Collectors.toCollection(
                                LinkedHashSet::new
                        )
                );
        String bestQuestion = null;

        double bestScore = -1;

        for (String question :
                possibleQuestions) {

            double score =
                    calculateDiscriminativePower(

                            question,

                            topCandidates,

                            knowledgeBase,

                            dominantCategory,

                            dominantVisualFamily
                    );

            // =====================================
            // VISUAL TRAIT BOOST
            // =====================================

            for (String trait :
                    dominantTraits) {

                if (
                        question.contains(trait)
                ) {

                    score += 15;
                }
            }

            // =====================================
            // MORPHOLOGY BOOST
            // =====================================

            if (
                    question.contains("ring")
                            ||
                            question.contains("plaque")
                            ||
                            question.contains("silvery")
                            ||
                            question.contains("central")
            ) {

                score += 10;
            }

            // =====================================
            // LOCATION BOOST
            // =====================================

            if (
                    question.contains(
                            "location"
                    )
            ) {

                score += 5;
            }

            // =====================================
            // GENERIC QUESTION PENALTY
            // =====================================

            if (
                    question.equals("itching")
                            ||
                            question.equals("redness")
                            ||
                            question.equals("burning")
            ) {

                score -= 5;
            }

            if (score > bestScore) {

                bestScore = score;

                bestQuestion = question;
            }
        }
        if (
        bestQuestion == null
        &&
        !possibleQuestions.isEmpty()
) {

    bestQuestion =
            possibleQuestions
                    .iterator()
                    .next();
}

        return bestQuestion;
    }

    // =====================================================
    // DISCRIMINATIVE POWER
    // =====================================================

    private static double
    calculateDiscriminativePower(

            String question,

            List<CandidateState> candidates,

            DiseaseKnowledgeBase knowledgeBase,

            String dominantCategory,

            String dominantVisualFamily
    ) {

        Map<String, List<Integer>>
                answerBuckets =
                new HashMap<>();

        double totalScore = 0;

        int rarityWeight = 1;

        for (CandidateState candidate :
                candidates) {

            DiseaseProfile profile =
                    knowledgeBase.get(
                            candidate.getDisease()
                    );

            if (profile == null) {
                continue;
            }

            Map<String, Map<String, Integer>>
                    signals =
                    profile.getSignalWeights();

            if (
                    !signals.containsKey(question)
            ) {

                continue;
            }

            // CATEGORY BOOST

            if (
                    profile.getCategory()
                            .equals(
                                    dominantCategory
                            )
            ) {

                totalScore += 5;
            }

            // VISUAL FAMILY BOOST

            if (
                    profile.getVisualFamily()
                            .equals(
                                    dominantVisualFamily
                            )
            ) {

                totalScore += 8;
            }

            // MORPHOLOGY BOOST

            for (
                    String morphology :
                    profile.getMorphology()
            ) {

                if (
                        question.contains(
                                morphology
                        )
                ) {

                    totalScore += 4;
                }
            }

            rarityWeight =
                    Math.max(

                            rarityWeight,

                            profile.getRarityWeight()
                    );

            Map<String, Integer> answers =
                    signals.get(question);

            for (
                    Map.Entry<String, Integer>
                            entry :
                    answers.entrySet()
            ) {

                answerBuckets

                        .computeIfAbsent(

                                entry.getKey(),

                                k -> new ArrayList<>()
                        )

                        .add(
                                entry.getValue()
                        );
            }
        }

        if (answerBuckets.isEmpty()) {
            return 0;
        }

        for (
                List<Integer> values :
                answerBuckets.values()
        ) {

            totalScore +=
                    calculateVariance(
                            values
                    );
        }

        totalScore +=
                answerBuckets.size()
                        * 0.75;

        totalScore +=
                calculateSeparationBonus(

                        question,

                        candidates,

                        knowledgeBase
                );

        totalScore *= rarityWeight;

        return totalScore;
    }

    // =====================================================
    // DOMINANT CATEGORY
    // =====================================================

    private static String dominantCategory(

            List<CandidateState> candidates,

            DiseaseKnowledgeBase kb
    ) {

        Map<String, Double>
                categoryScores =
                new HashMap<>();

        for (CandidateState candidate :
                candidates) {

            DiseaseProfile profile =
                    kb.get(
                            candidate.getDisease()
                    );

            if (profile == null) {
                continue;
            }

            categoryScores.merge(

                    profile.getCategory(),

                    candidate.getFinalScore(),

                    Double::sum
            );
        }

        return categoryScores

                .entrySet()

                .stream()

                .max(
                        Map.Entry
                                .comparingByValue()
                )

                .map(
                        Map.Entry::getKey
                )

                .orElse(null);
    }

    // =====================================================
    // DOMINANT VISUAL FAMILY
    // =====================================================

    private static String dominantVisualFamily(

            List<CandidateState> candidates,

            DiseaseKnowledgeBase kb
    ) {

        Map<String, Double>
                familyScores =
                new HashMap<>();

        for (CandidateState candidate :
                candidates) {

            DiseaseProfile profile =
                    kb.get(
                            candidate.getDisease()
                    );

            if (profile == null) {
                continue;
            }

            familyScores.merge(

                    profile.getVisualFamily(),

                    candidate.getFinalScore(),

                    Double::sum
            );
        }

        return familyScores

                .entrySet()

                .stream()

                .max(
                        Map.Entry
                                .comparingByValue()
                )

                .map(
                        Map.Entry::getKey
                )

                .orElse(null);
    }

    // =====================================================
    // EXTRACT DOMINANT TRAITS
    // =====================================================

    private static Set<String>
    extractDominantTraits(

            List<CandidateState> candidates
    ) {

        Set<String> traits =
                new HashSet<>();

        for (CandidateState candidate :
                candidates) {

            if (
                    candidate.getVisualTraits()
                            == null
            ) {

                continue;
            }

            VisualTraits visualTraits =
                    candidate.getVisualTraits();

            if (
                    visualTraits.getMorphology()
                            != null
            ) {

                traits.addAll(
                        visualTraits.getMorphology()
                );
            }

            if (
                    visualTraits.getTextures()
                            != null
            ) {

                traits.addAll(
                        visualTraits.getTextures()
                );
            }
        }

        return traits;
    }

    // =====================================================
    // TOP COMPETITOR SEPARATION
    // =====================================================

    private static double
    calculateSeparationBonus(

            String question,

            List<CandidateState> candidates,

            DiseaseKnowledgeBase knowledgeBase
    ) {

        if (candidates.size() < 2) {
            return 0;
        }

        CandidateState top1 =
                candidates.get(0);

        CandidateState top2 =
                candidates.get(1);

        DiseaseProfile p1 =
                knowledgeBase.get(
                        top1.getDisease()
                );

        DiseaseProfile p2 =
                knowledgeBase.get(
                        top2.getDisease()
                );

        if (p1 == null || p2 == null) {
            return 0;
        }

        Map<String, Integer> q1 =
                p1.getSignalWeights()
                        .get(question);

        Map<String, Integer> q2 =
                p2.getSignalWeights()
                        .get(question);

        if (q1 == null || q2 == null) {
            return 0;
        }

        double diff = 0;

        Set<String> allAnswers =
                new HashSet<>();

        allAnswers.addAll(
                q1.keySet()
        );

        allAnswers.addAll(
                q2.keySet()
        );

        for (String ans : allAnswers) {

            int v1 =
                    q1.getOrDefault(
                            ans,
                            0
                    );

            int v2 =
                    q2.getOrDefault(
                            ans,
                            0
                    );

            diff += Math.abs(v1 - v2);
        }

        return diff;
    }

    // =====================================================
    // VARIANCE
    // =====================================================

    private static double calculateVariance(
            List<Integer> values
    ) {

        if (values.isEmpty()) {
            return 0;
        }

        double mean =
                values.stream()
                        .mapToDouble(v -> v)
                        .average()
                        .orElse(0);

        double variance = 0;

        for (int value : values) {

            variance += Math.pow(
                    value - mean,
                    2
            );
        }

        return variance / values.size();
    }
}
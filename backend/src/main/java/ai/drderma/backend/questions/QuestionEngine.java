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

        // =====================================================
        // SORT CANDIDATES
        // =====================================================

        List<CandidateState> ranked =
                new ArrayList<>(candidates);

        ranked.sort((a, b) ->

                Double.compare(
                        b.getFinalScore(),
                        a.getFinalScore()
                )
        );

        // =====================================================
        // ONLY TOP COMPETITORS MATTER
        // =====================================================

        List<CandidateState> topCandidates =
                ranked.stream()

                        .limit(3)

                        .collect(
                                Collectors.toList()
                        );

        // =====================================================
        // IMAGE TRAITS
        // =====================================================

        Set<String> dominantTraits =
                extractDominantTraits(
                        topCandidates
                );

        // =====================================================
        // COLLECT QUESTIONS
        // =====================================================

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

        // =====================================================
        // SORT QUESTIONS
        // =====================================================

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

        // =====================================================
        // FIND BEST QUESTION
        // =====================================================

        String bestQuestion = null;

        double bestScore = -1;

        for (
                String question :
                possibleQuestions
        ) {

            double score =
                    calculateDiscriminativePower(

                            question,

                            topCandidates,

                            knowledgeBase
                    );

            // =====================================
            // IMAGE TRAIT BOOST
            // =====================================

            for (
                    String trait :
                    dominantTraits
            ) {

                if (
                        question.contains(
                                trait
                        )
                ) {

                    score += 20;
                }
            }

            // =====================================
            // MORPHOLOGY BOOST
            // =====================================

            if (
                    question.contains("ring")
                            ||
                            question.contains("central")
                            ||
                            question.contains("silvery")
                            ||
                            question.contains("plaque")
                            ||
                            question.contains("oozing")
                            ||
                            question.contains("blackheads")
            ) {

                score += 10;
            }

            // =====================================
            // GENERIC QUESTION PENALTY
            // =====================================

            if (
                    question.equals("itching")
                            ||
                            question.equals("burning")
                            ||
                            question.equals("redness")
            ) {

                score -= 8;
            }

            // =====================================
            // BEST
            // =====================================

            if (score > bestScore) {

                bestScore = score;

                bestQuestion = question;
            }
        }

        // =====================================================
        // FALLBACK
        // =====================================================

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

            DiseaseKnowledgeBase knowledgeBase
    ) {

        // =====================================
        // ONLY TOP 2 COMPETITORS
        // =====================================

        List<CandidateState> focused =
                candidates.stream()

                        .sorted(
                                (a, b) -> Double.compare(
                                        b.getFinalScore(),
                                        a.getFinalScore()
                                )
                        )

                        .limit(2)

                        .collect(
                                Collectors.toList()
                        );

        List<Integer> allWeights =
                new ArrayList<>();

        double totalScore = 0;

        for (
                CandidateState candidate :
                focused
        ) {

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

            Map<String, Integer> weights =
                    signals.get(question);

            // =====================================
            // EXCLUSIVE SIGNAL BONUS
            // =====================================

            boolean exclusiveSignal =
                    false;

            for (
                    CandidateState other :
                    focused
            ) {

                if (
                        other == candidate
                ) {

                    continue;
                }

                DiseaseProfile otherProfile =
                        knowledgeBase.get(
                                other.getDisease()
                        );

                if (otherProfile == null) {
                    continue;
                }

                if (
                        !otherProfile
                                .getSignalWeights()
                                .containsKey(question)
                ) {

                    exclusiveSignal = true;
                }
            }

            if (exclusiveSignal) {

                totalScore += 25;
            }

            allWeights.addAll(
                    weights.values()
            );
        }

        // =====================================
        // NOT ENOUGH INFO
        // =====================================

        if (
                allWeights.size() < 2
        ) {

            return totalScore;
        }

        // =====================================
        // VARIANCE
        // =====================================

        double mean =
                allWeights.stream()

                        .mapToDouble(i -> i)

                        .average()

                        .orElse(0);

        double variance = 0;

        for (int weight : allWeights) {

            variance +=
                    Math.pow(
                            weight - mean,
                            2
                    );
        }

        variance /=
                allWeights.size();

        totalScore += variance;

        // =====================================
        // TOP COMPETITOR SEPARATION
        // =====================================

        totalScore +=
                calculateSeparationBonus(

                        question,

                        focused,

                        knowledgeBase
                );

        return totalScore;
    }

    // =====================================================
    // IMAGE TRAITS
    // =====================================================

    private static Set<String>
    extractDominantTraits(

            List<CandidateState> candidates
    ) {

        Set<String> traits =
                new HashSet<>();

        for (
                CandidateState candidate :
                candidates
        ) {

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

            if (
                    visualTraits.getColors()
                            != null
            ) {

                traits.addAll(
                        visualTraits.getColors()
                );
            }
        }

        return traits;
    }

    // =====================================================
    // SEPARATION BONUS
    // =====================================================

    private static double
    calculateSeparationBonus(

            String question,

            List<CandidateState> candidates,

            DiseaseKnowledgeBase knowledgeBase
    ) {

        if (
                candidates.size() < 2
        ) {

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

        if (
                p1 == null
                        ||
                        p2 == null
        ) {

            return 0;
        }

        Map<String, Integer> q1 =
                p1.getSignalWeights()
                        .get(question);

        Map<String, Integer> q2 =
                p2.getSignalWeights()
                        .get(question);

        if (
                q1 == null
                        ||
                        q2 == null
        ) {

            return 0;
        }

        double diff = 0;

        Set<String> answers =
                new HashSet<>();

        answers.addAll(
                q1.keySet()
        );

        answers.addAll(
                q2.keySet()
        );

        for (
                String answer :
                answers
        ) {

            int v1 =
                    q1.getOrDefault(
                            answer,
                            0
                    );

            int v2 =
                    q2.getOrDefault(
                            answer,
                            0
                    );

            diff +=
                    Math.abs(
                            v1 - v2
                    );
        }

        return diff * 2;
    }
}
package ai.drderma.backend.engine;

import java.util.*;
import java.util.stream.Collectors;

public class ImageSimilarityEngine {

    public static Map<String, Double> topDiseaseSimilarities(
            double[] query,
            List<double[]> vectors,
            List<String> labels,
            int topK
    ) {
        Map<String, Double> bestSimilarityPerDisease = new HashMap<>();

        for (int i = 0; i < vectors.size(); i++) {
            double similarity = cosineSimilarity(query, vectors.get(i));
            String disease = labels.get(i);

            bestSimilarityPerDisease.merge(
                    disease,
                    similarity,
                    Math::max
            );
        }

        return bestSimilarityPerDisease.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(topK)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    private static double cosineSimilarity(double[] a, double[] b) {
    double dot = 0.0;

    for (int i = 0; i < a.length; i++) {
        dot += a[i] * b[i];
    }
    if (a.length != b.length) {
    throw new IllegalArgumentException("Vector size mismatch");
}

    return dot; // vectors already L2 normalized
}


}

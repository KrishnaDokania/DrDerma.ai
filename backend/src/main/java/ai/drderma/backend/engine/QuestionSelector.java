package ai.drderma.backend.engine;

import ai.drderma.backend.model.DiseaseProfile;

import java.util.*;
import org.springframework.stereotype.Component;

@Component

public class QuestionSelector {

    public String selectBestSignal(
        List<DiseaseProfile> profiles,
        Set<String> askedSignals
) {

    String bestSignal = null;
    double bestVariance = -1;

    for (DiseaseProfile profile : profiles) {
        for (String signal : profile.getSignalWeights().keySet()) {

            if (askedSignals.contains(signal)) continue;

            List<Integer> weights = new ArrayList<>();

            for (DiseaseProfile p : profiles) {
                int weight = p.getSignalWeights()
                        .getOrDefault(signal, Map.of())
                        .values()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .max()
                        .orElse(0);

                weights.add(weight);
            }

            double variance = calculateVariance(weights);

            if (variance > bestVariance) {
                bestVariance = variance;
                bestSignal = signal;
            }
        }
    }

    return bestSignal;
}
private double calculateVariance(List<Integer> values) {
    double mean = values.stream()
            .mapToDouble(i -> i)
            .average()
            .orElse(0.0);

    double variance = 0;
    for (int v : values) {
        variance += Math.pow(v - mean, 2);
    }

    return variance / values.size();
}

}

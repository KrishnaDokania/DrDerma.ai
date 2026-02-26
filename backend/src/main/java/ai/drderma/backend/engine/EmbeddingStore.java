package ai.drderma.backend.engine;

import ai.drderma.backend.model.StoredEmbedding;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmbeddingStore {

    private final List<double[]> vectors = new ArrayList<>();
    private final List<String> labels = new ArrayList<>();

    @PostConstruct
    public void load() {
        System.out.println("EmbeddingStore load() CALLED");

        try {
            ObjectMapper mapper = new ObjectMapper();

            File file = new File(
                "C:/Users/ilfan/Downloads/DrDerma.Ai/data/embeddings/centroids.json"
            );

            StoredEmbedding[] data = mapper.readValue(file, StoredEmbedding[].class);

            for (StoredEmbedding e : data) {

                // Convert List<Double> → double[]
                double[] centroidArray = e.getCentroidArray();

                vectors.add(centroidArray);

                // Store disease name as label
                labels.add(e.getDisease());
            }

            System.out.println("Loaded centroids: " + vectors.size());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load embeddings", e);
        }
    }

    public List<double[]> getVectors() {
        return vectors;
    }

    public List<String> getLabels() {
        return labels;
    }
}

package ai.drderma.backend.model;

import java.util.List;

public class StoredEmbedding {

    private String disease;
    private List<Double> centroid;

    public String getDisease() {
        return disease;
    }

    public List<Double> getCentroid() {
        return centroid;
    }

    // Optional: convert to primitive array for faster cosine computation
    public double[] getCentroidArray() {
        double[] arr = new double[centroid.size()];
        for (int i = 0; i < centroid.size(); i++) {
            arr[i] = centroid.get(i);
        }
        return arr;
    }
}

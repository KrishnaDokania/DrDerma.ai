package ai.drderma.backend.model;

import java.util.List;

public class Feature {

    private String key;

    private String question;

  private FeatureType type;

    private List<String> options;

    public Feature() {
    }

    public Feature(
        String key,
        String question,
        FeatureType type,
        List<String> options
){

        this.key = key;
        this.question = question;
        this.type = type;
        this.options = options;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

   public FeatureType getType() {
        return type;
    }

 public void setType(
        FeatureType type
) {
        this.type = type;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(
            List<String> options
    ) {

        this.options = options;
    }
}

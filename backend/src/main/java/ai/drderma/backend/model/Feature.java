package ai.drderma.backend.model;

import java.util.List;

public class Feature {

    private String key;

    private String question;

private String type;

    private List<String> options;

    public Feature() {
    }

public Feature(

        String key,

        String question,

        FeatureType type,

        List<String> options
) {

    this.key = key;

    this.question = question;

    this.type = type.name();

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

  public String getType() {
    return type;
}

public void setType(String type) {
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

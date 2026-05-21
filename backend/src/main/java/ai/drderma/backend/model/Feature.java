package ai.drderma.backend.model;

import java.util.List;

public class Feature {

    // =====================================================
    // KEY
    // =====================================================

    private String key;

    // =====================================================
    // QUESTION TEXT
    // =====================================================

    private String question;

    // =====================================================
    // TYPE
    // =====================================================

    private String type;

    // =====================================================
    // OPTIONS
    // =====================================================

    private List<String> options;

    // =====================================================
    // CONSTRUCTORS
    // =====================================================

    public Feature() {
    }

    public Feature(

            String key,

            String question,

            FeatureType type,

            List<String> options
    ) {

        this.key =
                key;

        this.question =
                question;

        this.type =
                type.name();

        this.options =
                options;
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public String getKey() {
        return key;
    }

    public String getQuestion() {
        return question;
    }

    public String getType() {
        return type;
    }

    public List<String> getOptions() {
        return options;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setKey(
            String key
    ) {

        this.key =
                key;
    }

    public void setQuestion(
            String question
    ) {

        this.question =
                question;
    }

    public void setType(
            String type
    ) {

        this.type =
                type;
    }

    public void setOptions(
            List<String> options
    ) {

        this.options =
                options;
    }
}
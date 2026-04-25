package ai.drderma.backend.model;

public class DiseaseQuestion {

    private String disease;

    private String question;

    public DiseaseQuestion(String disease, String question) {
        this.disease = disease;
        this.question = question;
    }

    public String getDisease() {
        return disease;
    }

    public String getQuestion() {
        return question;
    }
}
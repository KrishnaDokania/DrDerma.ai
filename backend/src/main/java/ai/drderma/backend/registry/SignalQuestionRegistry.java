package ai.drderma.backend.registry;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SignalQuestionRegistry {

    private final Map<String, Map<String, Object>> questions = Map.ofEntries(

        Map.entry("itching", Map.of(
                "text", "Does the affected area feel itchy?",
                "options", List.of("yes", "no")
        )),

        Map.entry("scaling", Map.of(
                "text", "Is there visible scaling or flaking?",
                "options", List.of("yes", "no")
        )),

        Map.entry("oozing", Map.of(
                "text", "Is there any fluid discharge or oozing?",
                "options", List.of("yes", "no")
        )),

        Map.entry("redness", Map.of(
                "text", "Is the area red or inflamed?",
                "options", List.of("yes", "no")
        )),

        Map.entry("central_clearing", Map.of(
                "text", "Does the center of the lesion appear clearer than the edges?",
                "options", List.of("yes", "no")
        )),

        Map.entry("silvery_scale", Map.of(
                "text", "Are the scales thick and silvery in appearance?",
                "options", List.of("yes", "no")
        )),

        Map.entry("well_defined_border", Map.of(
                "text", "Are the borders of the lesion clearly defined?",
                "options", List.of("yes", "no")
        )),

        Map.entry("burning_sensation", Map.of(
                "text", "Do you feel a burning sensation in the affected area?",
                "options", List.of("yes", "no")
        )),
        Map.entry("sweating", Map.of(
        "text", "Does the condition worsen with sweating?",
        "options", List.of("yes", "no")
         )),


        Map.entry("location", Map.of(
                "text", "Where is the issue mainly located?",
                "options", List.of("face", "groin", "arms", "legs", "trunk")
        ))
    );

    public Map<String, Object> getQuestion(String signal) {
        return questions.get(signal);
    }

    public Set<String> getAllSignals() {
        return questions.keySet();
    }
}

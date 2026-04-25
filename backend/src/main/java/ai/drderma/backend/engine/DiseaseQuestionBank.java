package ai.drderma.backend.engine;

import java.util.List;
import java.util.Map;

public class DiseaseQuestionBank {

    private static final Map<String, List<String>> QUESTIONS = Map.ofEntries(

        Map.entry("tinea_corporis", List.of(
                "Is the rash circular in shape?",
                "Does the rash have a red raised border?",
                "Is the center of the rash clearer than the edges?"
        )),

        Map.entry("tinea_capitis", List.of(
                "Is there hair loss in the affected scalp area?",
                "Are there scaly patches on the scalp?",
                "Is the scalp itchy?"
        )),

        Map.entry("tinea_pedis", List.of(
                "Is there itching between the toes?",
                "Is the skin between the toes peeling or cracked?",
                "Is there burning sensation on the feet?"
        )),

        Map.entry("guttate_psoriasis", List.of(
                "Are there small drop-shaped red spots on the skin?",
                "Did the rash appear suddenly?",
                "Are the spots covered with fine scales?"
        )),

        Map.entry("psoriasis_plaque", List.of(
                "Are there thick red patches on the skin?",
                "Do the patches have silvery scales?",
                "Do the patches bleed when scratched?"
        )),

        Map.entry("psoriasis_vulgaris", List.of(
                "Are there raised red patches with scales?",
                "Are the patches located on elbows or knees?",
                "Do the patches itch or burn?"
        )),

        Map.entry("contact_dermatitis", List.of(
                "Did the rash appear after touching a chemical or substance?",
                "Is the rash itchy or burning?",
                "Is the rash located only where the skin contacted something?"
        )),

        Map.entry("vitiligo", List.of(
                "Are there white patches on the skin?",
                "Are the patches sharply defined?",
                "Did the patches spread gradually?"
        )),

        Map.entry("impetigo", List.of(
                "Are there red sores that burst and form yellow crusts?",
                "Are the sores spreading quickly?",
                "Are the sores mostly around the mouth or nose?"
        )),

        Map.entry("chickenpox", List.of(
                "Are there small fluid-filled blisters on the skin?",
                "Did the rash start with fever or fatigue?",
                "Are the blisters appearing all over the body?"
        )),

        Map.entry("cystic_acne", List.of(
                "Are there large painful bumps under the skin?",
                "Do the bumps contain pus?",
                "Are the bumps deep and inflamed?"
        )),

        Map.entry("acne_vulgaris", List.of(
                "Are there pimples on the skin?",
                "Are there blackheads or whiteheads?",
                "Is the affected area oily?"
        )),

        Map.entry("acne", List.of(
                "Are there pimples on the face or back?",
                "Are there clogged pores or blackheads?",
                "Is the skin oily?"
        )),

        Map.entry("scabies", List.of(
                "Is the itching worse at night?",
                "Are there small burrow-like lines on the skin?",
                "Do other people around you have similar itching?"
        )),

        Map.entry("seborrheic_dermatitis", List.of(
                "Are there greasy yellowish scales on the skin?",
                "Is the rash on scalp or eyebrows?",
                "Is the skin flaky?"
        )),

        Map.entry("melasma", List.of(
                "Are there dark brown patches on the face?",
                "Are the patches symmetrical on both sides of the face?",
                "Did the pigmentation increase after sun exposure?"
        )),

        Map.entry("common_warts", List.of(
                "Are there rough raised bumps on the skin?",
                "Do the bumps have a cauliflower-like texture?",
                "Are the bumps located on hands or fingers?"
        )),

        Map.entry("atopic_dermatitis", List.of(
                "Is the skin very itchy?",
                "Is the skin dry and cracked?",
                "Did the condition start in childhood?"
        )),

        Map.entry("eczema_atopic_dermatitis", List.of(
                "Is the skin extremely itchy?",
                "Is the skin dry and inflamed?",
                "Are the rashes located on the inner elbows or knees?"
        )),

        Map.entry("rosacea", List.of(
                "Is there redness on the face?",
                "Do you experience facial flushing?",
                "Are small blood vessels visible on the face?"
        )),

        Map.entry("urticaria", List.of(
                "Do raised itchy welts appear on the skin?",
                "Do the welts change location quickly?",
                "Is the itching severe?"
        )),

        Map.entry("herpes_simplex", List.of(
                "Are there painful fluid-filled blisters?",
                "Do the blisters appear around the lips or genitals?",
                "Do the blisters break and form crusts?"
        ))

    );

    public static List<String> getQuestions(String disease) {
        return QUESTIONS.getOrDefault(disease, List.of());
    }
}
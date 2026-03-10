package ai.drderma.backend.engine;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import ai.drderma.backend.exception.UnsupportedImageException;

import java.util.List;
import java.util.Map;

@Component
public class MlClient {

    private static final String EMBED_URL = "http://localhost:8001/embed";
    private static final String SKIN_CHECK_URL = "http://localhost:8001/skin-check";

    private final RestTemplate restTemplate = new RestTemplate();

    // ===============================
    // SKIN CHECK
    // ===============================
    @SuppressWarnings("unchecked")
    private boolean isSkin(MultipartFile image) {

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            ByteArrayResource resource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("file", resource);

            HttpEntity<MultiValueMap<String, Object>> request =
                    new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(SKIN_CHECK_URL, request, Map.class);

            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("is_skin")) {
                throw new RuntimeException("Invalid response from ML service");
            }

            Boolean isSkin = (Boolean) responseBody.get("is_skin");

            return Boolean.TRUE.equals(isSkin);

        } catch (Exception e) {
            throw new RuntimeException("Skin check failed", e);
        }
    }

    // ===============================
    // EMBEDDING
    // ===============================
    @SuppressWarnings("unchecked")
    public double[] embed(MultipartFile image) {

        if (!isSkin(image)) {
            throw new UnsupportedImageException(
                    "Uploaded image does not appear to be a skin condition."
            );
        }

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            ByteArrayResource resource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("file", resource);

            HttpEntity<MultiValueMap<String, Object>> request =
                    new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(EMBED_URL, request, Map.class);

            Map<String, Object> body = response.getBody();

            if (body == null || !body.containsKey("vector")) {
                throw new RuntimeException("Invalid response from ML service");
            }

            List<Number> vector = (List<Number>) body.get("vector");

            double[] result = new double[vector.size()];

            for (int i = 0; i < vector.size(); i++) {
                result[i] = vector.get(i).doubleValue();
            }

            if (result.length != 1280) {
                throw new RuntimeException("Invalid embedding size: " + result.length);
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to call ML service", e);
        }
    }
}
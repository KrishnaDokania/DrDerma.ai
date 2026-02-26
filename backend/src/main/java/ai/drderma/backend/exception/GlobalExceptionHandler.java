package ai.drderma.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnsupportedImageException.class)
    public ResponseEntity<?> handleUnsupportedImage(
            UnsupportedImageException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", "unsupported",
                        "message", ex.getMessage()
                ));
    }
}

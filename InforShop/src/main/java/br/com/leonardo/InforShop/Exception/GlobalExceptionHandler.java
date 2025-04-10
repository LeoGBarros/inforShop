package br.com.leonardo.InforShop.Exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Validações do @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Erro de validação");

        Map<String, String> messages = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                messages.put(err.getField(), err.getDefaultMessage())
        );

        response.put("message", messages);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 2. Campo com tipo inválido (ex: "price": "abc")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonFormatError(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Erro de leitura do JSON");

        String message = "Erro ao interpretar a requisição. Verifique se os campos estão no formato correto.";

        if (ex.getCause() instanceof InvalidFormatException cause) {
            String field = cause.getPath().get(0).getFieldName();
            String expectedType = traduzirTipo(cause.getTargetType().getSimpleName());
            message = String.format("O campo '%s' precisa ser do tipo %s.", field, expectedType);
        }

        response.put("message", message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 3. Campo desconhecido (ex: "CampoInexistente": "123")
    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<Map<String, Object>> handleUnrecognizedProperty(UnrecognizedPropertyException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Campo inválido na requisição");

        String field = ex.getPropertyName();
        response.put("message", String.format("O campo '%s' não é reconhecido e não deve ser enviado.", field));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 4. Outros erros inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Erro interno do servidor");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Utilitário para tradução de tipos
    private String traduzirTipo(String tipo) {
        return switch (tipo) {
            case "Double" -> "número real (ex: 99.99)";
            case "Integer" -> "número inteiro";
            case "Long" -> "número inteiro longo";
            case "Boolean" -> "verdadeiro ou falso";
            default -> tipo.toLowerCase();
        };
    }
}

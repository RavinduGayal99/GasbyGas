package cw.gasbygas.exceptionHandling;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private Map<String, String> errors;
}
package com.vsk.clinic_backend.response;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

public class ClinicValidationExceptionResponse {
    public HashMap<String, Map<String, String>> getResponse(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        HashMap<String, Map<String, String>> output = new HashMap<>();
        output.put("errors", errors);
        return output;
    }
}

package com.vsk.clinic_backend.response;


import com.vsk.clinic_backend.exception.ClinicNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class ClinicNotFoundExceptionResponse {
    public HashMap<String, Map<String, String>> getResponse(ClinicNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("clinic", ex.getMessage());

        HashMap<String, Map<String, String>> output = new HashMap<>();
        output.put("errors", errors);
        return output;
    }
}

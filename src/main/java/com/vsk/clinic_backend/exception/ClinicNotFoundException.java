package com.vsk.clinic_backend.exception;

public class ClinicNotFoundException extends RuntimeException {

    public ClinicNotFoundException(Long id) {
        super(ClinicNotFoundException.getError(id));
    }

    private static String getError(Long id) {
        return String.format("Could not find clinic with id %d", id);
    }
}

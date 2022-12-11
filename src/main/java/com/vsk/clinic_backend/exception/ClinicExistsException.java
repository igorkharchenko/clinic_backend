package com.vsk.clinic_backend.exception;

public class ClinicExistsException extends RuntimeException {
    public ClinicExistsException(String code) {
        super(ClinicExistsException.getError(code));
    }

    private static String getError(String code) {
        return String.format("Clinic with the code %s already exists.", code);
    }
}

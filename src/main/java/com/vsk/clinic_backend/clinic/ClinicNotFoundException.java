package com.vsk.clinic_backend.clinic;

public class ClinicNotFoundException extends RuntimeException {

    public ClinicNotFoundException(Long id) {
        super("Could not find clinic " + id);
    }
}

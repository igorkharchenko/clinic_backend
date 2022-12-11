package com.vsk.clinic_backend.controller;

import com.vsk.clinic_backend.component.ClinicModelAssembler;
import com.vsk.clinic_backend.entity.Clinic;
import com.vsk.clinic_backend.exception.ClinicExistsException;
import com.vsk.clinic_backend.exception.ClinicNotFoundException;
import com.vsk.clinic_backend.repository.ClinicRepository;
import com.vsk.clinic_backend.response.ClinicExistsExceptionResponse;
import com.vsk.clinic_backend.response.ClinicNotFoundExceptionResponse;
import com.vsk.clinic_backend.response.ClinicValidationExceptionResponse;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ClinicController {
    private final ClinicRepository repository;

    private final ClinicModelAssembler assembler;

    ClinicController(ClinicRepository repository, ClinicModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/clinics")
    public CollectionModel<EntityModel<Clinic>> all() {
        List<EntityModel<Clinic>> clinics = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(clinics);
    }

    @PostMapping("/clinics")
    public ResponseEntity<?> newClinic(@Valid @RequestBody Clinic newClinic) {
        String newClinicCode = newClinic.getCode();
        // если уже есть клиника с таким кодом - новая клиника не может быть создана
        if (!repository.findByCodeEquals(newClinicCode).isEmpty()) {
            throw new ClinicExistsException(newClinicCode);
        }

        EntityModel<Clinic> entityModel = assembler.toModel(repository.save(newClinic));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/clinics/{id}")
    public EntityModel<Clinic> one(@PathVariable Long id) {
        Clinic clinic = repository
                .findById(id)
                .orElseThrow(() -> new ClinicNotFoundException(id));

        return assembler.toModel(clinic);
    }

    @PutMapping("/clinics/{id}")
    public ResponseEntity<?> replaceClinic(@Valid @RequestBody Clinic newClinic, @PathVariable Long id) {
        String newClinicCode = newClinic.getCode();
        List<Clinic> clinics = repository.findByCodeEquals(newClinicCode);

        // если уже есть клиника с таким кодом - новая клиника не может быть создана
        if (!clinics.isEmpty() && !clinics.get(0).getId().equals(id)) {
            throw new ClinicExistsException(newClinicCode);
        }

        Clinic updatedClinic = repository
                .findById(id)
                .map(clinic -> {
                    clinic.setName(newClinic.getName());
                    clinic.setCode(newClinic.getCode());

                    return repository.save(clinic);
                })
                .orElseGet(() -> {
                    newClinic.setId(id);

                    return repository.save(newClinic);
                });

        EntityModel<Clinic> entityModel = assembler.toModel(updatedClinic);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/clinics/{id}")
    public void deleteClinic(@PathVariable Long id) {
        repository
                .findById(id)
                .orElseThrow(() -> new ClinicNotFoundException(id));

        repository.deleteById(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap<String, Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        return (new ClinicValidationExceptionResponse()).getResponse(ex);
    }

    @ExceptionHandler(ClinicExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap<String, Map<String, String>> handleClinicExistsException(ClinicExistsException ex) {
        return (new ClinicExistsExceptionResponse()).getResponse(ex);
    }

    @ExceptionHandler(ClinicNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    HashMap<String, Map<String, String>> handleClinicNotFoundException(ClinicNotFoundException ex) {
        return (new ClinicNotFoundExceptionResponse()).getResponse(ex);
    }

}
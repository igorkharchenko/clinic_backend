package com.vsk.clinic_backend.component;

import com.vsk.clinic_backend.controller.ClinicController;
import com.vsk.clinic_backend.entity.Clinic;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ClinicModelAssembler implements RepresentationModelAssembler<Clinic, EntityModel<Clinic>>  {

    @Override
    public @NotNull EntityModel<Clinic> toModel(@NotNull Clinic clinic) {
        return EntityModel.of(clinic,
                linkTo(methodOn(ClinicController.class).one(clinic.getId())).withSelfRel(),
                linkTo(methodOn(ClinicController.class).all()).withRel("clinics"));
    }
}

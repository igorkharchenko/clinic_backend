package com.vsk.clinic_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name="clinic")
public class Clinic {

    private @Id
    @GeneratedValue(strategy = GenerationType.AUTO) Long id;

    @NotBlank(message = "Name is mandatory.")
    @Size(min = 2, max = 255, message = "The name must be at least 2 and not more than 255 characters.")
    @Pattern(regexp = "[A-Za-zА-ЯЁа-яё\\d,;:.\" -]+", message = "The name can contain only letters, spaces and digits, or one of this characters: ,-;:\".")
    private String name;

    @NotBlank(message = "Code is mandatory.")
    @Pattern(regexp = "\\d{2}[A-Za-zА-ЯЁа-яё]{2}\\d{5}", message = "The code must be in format 'DDCCDDDDD', where D - digit, C - alphabetical character.")
    private String code;

    public Clinic() {
    }

    public Clinic(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof Clinic clinic)) {
            return false;
        }
        return Objects.equals(this.id, clinic.id)
                && Objects.equals(this.name, clinic.name)
                && Objects.equals(this.code, clinic.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.code);
    }
}
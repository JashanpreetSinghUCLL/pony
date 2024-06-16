package com.UCLLBackEnd.pony.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PastOrPresent(message = "Registration date cannot be in future")
    private LocalDate registrationDate;

    private LocalDate closingDate;

    @NotEmpty(message = "Description must be given")
    private String description;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    public MedicalRecord(LocalDate registrationDate, String description) {
        this.registrationDate = registrationDate;
        this.description = description;
    }

    public MedicalRecord() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {

        if (!closingDate.isAfter(getRegistrationDate())) {
            throw new DomainException("DomainException", "Closing date should be after registration date");
        }
        this.closingDate = closingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "id=" + id +
                ", registrationDate=" + registrationDate +
                ", closingDate=" + closingDate +
                ", description='" + description + '\'' +
                '}';
    }
}

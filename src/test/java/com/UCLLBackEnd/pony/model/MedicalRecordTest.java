package com.UCLLBackEnd.pony.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MedicalRecordTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {

        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    public void givenRegistrationDateInFuture_whenCreatingMedicalRecord_thenExceptionIsThrown() {

        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.of(2024, 12, 10), "Ate too much");

        Set<ConstraintViolation<MedicalRecord>> violations = validator.validate(medicalRecord);

        assertFalse(violations.isEmpty());

        String message = violations.iterator().next().getMessage();

        assertEquals("Registration date cannot be in future", message);
    }

    @Test
    public void givenRegistrationNotEmpty_whenCreatingMedicalRecord_thenExceptionIsThrown() {

        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.of(2024, 2, 10), null);

        Set<ConstraintViolation<MedicalRecord>> violations = validator.validate(medicalRecord);

        assertFalse(violations.isEmpty());

        String message = violations.iterator().next().getMessage();

        assertEquals("Description must be given", message);
    }

    @Test
    public void givenValidMedicalRecord_whenCreatingMedicalRecord_thenMedicalRecordIsCreated() {

        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.of(2024, 2, 10), "Ate too much");

        assertEquals(LocalDate.of(2024, 2, 10), medicalRecord.getRegistrationDate()) ;
        assertEquals("Ate too much", medicalRecord.getDescription());
    }

    @Test
    public void givenInvalidClosingDate_whenSettingClosingDateToMedicalRecord_thenExceptionIsThrown() {

        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.of(2024, 2, 10), "Ate too much");

        DomainException exception = assertThrows(DomainException.class,
                () -> medicalRecord.setClosingDate(LocalDate.of(2024, 2, 10)));

        assertEquals("DomainException", exception.getField());
        assertEquals("Closing date should be after registration date", exception.getMessage());
    }
}
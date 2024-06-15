package com.UCLLBackEnd.pony.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StableTest {

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
    public void givenNoName_whenCreatingStable_thenThrowsException() {

        Stable stable = new Stable("", 5);

        Set<ConstraintViolation<Stable>> violations = validator.validate(stable);
        assertFalse(violations.isEmpty());
        String message = violations.iterator().next().getMessage();
        assertEquals("The name of stable is required", message);
    }


    @Test
    public void givenInvalidAge_whenCreatingStable_thenThrowsException() {

        Stable stable = new Stable("Farm Frenzy", -1);

        Set<ConstraintViolation<Stable>> violations = validator.validate(stable);
        assertFalse(violations.isEmpty());
        String message = violations.iterator().next().getMessage();
        assertEquals("Max number of animals should be 0 or more", message);
    }

    @Test
    public void givenValidStable_whenCreatingStable_thenStableIsCreated() {

        Stable stable = new Stable("Farm Frenzy", 5);

        assertEquals("Farm Frenzy", stable.getName());
        assertEquals(5, stable.getMax_number_of_animals());
    }
}
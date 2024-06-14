package com.UCLLBackEnd.pony.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AnimalTest {

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
    public void givenNoName_whenCreateAnimal_thenThrowsException() {

        Animal animal = new Animal("", 9);

        Set<ConstraintViolation<Animal>> violations = validator.validate(animal);

        assertFalse(violations.isEmpty());
        String message = violations.iterator().next().getMessage();
        assertEquals("Name is required.", message);
    }

    @Test
    public void givenUnderAge_whenCreateAnimal_thenThrowsException() {

        Animal animal = new Animal("Rambo", 0);

        Set<ConstraintViolation<Animal>> violations = validator.validate(animal);

        assertFalse(violations.isEmpty());
        String message = violations.iterator().next().getMessage();
        assertEquals("Minimum age is 1", message);
    }


    @Test
    public void givenOverAge_whenCreateAnimal_thenThrowsException() {

        Animal animal = new Animal("Rambo", 51);

        Set<ConstraintViolation<Animal>> violations = validator.validate(animal);

        assertFalse(violations.isEmpty());
        String message = violations.iterator().next().getMessage();
        assertEquals("Maximum age is 50", message);
    }

    @Test
    public void givenValidAnimal_whenCreateAnimal_thenAnimalIsCreated() {

        Animal animal = new Animal("Rambo", 13);

        assertEquals("Rambo", animal.getName());
        assertEquals(13, animal.getAge());
    }
}
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

class AddressTest {

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
    public void givenNotEmptyStreet_whenCreatingAddress_thenExceptionIsThrown() {

        Address address = new Address("", 3, "Hasselt");

        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        assertFalse(violations.isEmpty());
        String message = violations.iterator().next().getMessage();

        assertEquals("Street must be given", message);
    }

    @Test
    public void givenNegativeNumber_whenCreatingAddress_thenExceptionIsThrown() {

        Address address = new Address("Hellostraat", -1, "Hasselt");

        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        assertFalse(violations.isEmpty());
        String message = violations.iterator().next().getMessage();

        assertEquals("Number must be atleast 1", message);
    }

    @Test
    public void givenNumber_whenCreatingAddress_thenExceptionIsThrown() {

        Address address = new Address("Hellostraat", 2, "");

        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        assertFalse(violations.isEmpty());
        String message = violations.iterator().next().getMessage();

        assertEquals("The place must be given", message);
    }

    @Test
    public void givenValidAddress_whenCreatingAddress_thenAddressIsCreated() {

        Address address = new Address("Hellostraat", 3, "Hasselt");

        assertEquals("Hellostraat", address.getStreet());
        assertEquals(3, address.getNumber());
        assertEquals("Hasselt", address.getPlace());
    }
}
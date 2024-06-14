package com.UCLLBackEnd.pony.controller;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.model.DomainException;
import com.UCLLBackEnd.pony.service.AnimalService;
import com.UCLLBackEnd.pony.service.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/animals")
public class AnimalRestController {

    private AnimalService animalService;

    @Autowired
    public AnimalRestController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping
    public Animal postAnimal(@Valid @RequestBody Animal animal) {
        return animalService.addAnimal(animal);
    }

    @GetMapping
    public List<Animal> getAllAnimals() {
        return animalService.getAllAnimals();
    }

    @GetMapping("/age/{age}")
    public List<Animal> getAnimalsOlderThan(@PathVariable int age) {
        return animalService.getAllAnimalsOlderThan(age);
    }

    @GetMapping("/age/oldest")
    public Animal getOldestAnimal() {
        return animalService.getOldestAnimal();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DomainException.class)
    public Map<String, String> handleDomainExceptions(DomainException domainException) {
        Map<String, String> errors = new HashMap<>();
        errors.put(domainException.getField(), domainException.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public Map<String, String> handleServiceExceptions(ServiceException serviceException) {
        Map<String, String> errors = new HashMap<>();
        errors.put(serviceException.getField(), serviceException.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

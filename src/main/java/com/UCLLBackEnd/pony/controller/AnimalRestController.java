package com.UCLLBackEnd.pony.controller;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.model.DomainException;
import com.UCLLBackEnd.pony.model.Stable;
import com.UCLLBackEnd.pony.model.Toy;
import com.UCLLBackEnd.pony.service.AnimalService;
import com.UCLLBackEnd.pony.service.ServiceException;
import com.UCLLBackEnd.pony.service.StableService;
import com.UCLLBackEnd.pony.service.ToyService;
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

    private ToyService toyService;
    private AnimalService animalService;

    private StableService stableService;

    @Autowired
    public AnimalRestController(AnimalService animalService, StableService stableService, ToyService toyService) {
        this.animalService = animalService;
        this.stableService = stableService;
        this.toyService = toyService;
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

    @PostMapping("/{animalName}/stable")
    public Stable addNewStableAnimal(@PathVariable String animalName, @Valid @RequestBody Stable stable) {
        return stableService.assignAnimalToNewStable(animalName, stable);
    }

    @PostMapping("/{animalName}")
    public Stable addExistingStableToAnimal(@PathVariable String animalName, @RequestParam Long stableId) {
        return stableService.assignAnimalToExistingStable(animalName, stableId);
    }

    @GetMapping("/stablesWithAnimals")
    public List<Stable> getAllStablesWithAnimalsSleepingInThem() {
        return stableService.getAllStablesWithAnimalsSleepingInThem();
    }

    @GetMapping("/{animalName}/stable")
    public Stable getStableOfAnimal(@PathVariable String animalName) {
        return stableService.getStableOfAnimal(animalName);
    }

    @PostMapping("/toys")
    public Toy addNewToyAnimal(@Valid @RequestBody Toy toy) {
        return toyService.addToy(toy);
    }

    @PostMapping("/{animalId}/{toyId}")
    public Toy addAnimalToToy(@PathVariable Long animalId, @PathVariable Long toyId) {
        return toyService.addAnimalToToy(animalId, toyId);
    }

    @GetMapping("/toys/{toyName}")
    public Toy getToyWithSpecificName(@PathVariable String toyName) {
        return toyService.getToyWithSpecificName(toyName);
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

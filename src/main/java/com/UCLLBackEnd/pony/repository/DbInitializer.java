package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.Animal;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DbInitializer {

    private AnimalRepository animalRepository;

    @Autowired
    public DbInitializer(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @PostConstruct
    public void init() {
        addAnimals();
    }

    private void addAnimals() {

        List<Animal> animals = new ArrayList<>(
                List.of(
                        new Animal("Bella", 20),
                        new Animal("Luna", 10),
                        new Animal("Muriel", 2),
                        new Animal("Little", 1)
                )
        );

        animalRepository.saveAll(animals);
    }
}

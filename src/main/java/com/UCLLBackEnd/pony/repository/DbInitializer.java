package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.model.Stable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DbInitializer {

    private AnimalRepository animalRepository;

    private StableRepository stableRepository;

    @Autowired
    public DbInitializer(AnimalRepository animalRepository, StableRepository stableRepository) {
        this.animalRepository = animalRepository;
        this.stableRepository = stableRepository;
    }

    @PostConstruct
    public void init() {
        addStables();
        addAnimals();
    }

    private void addStables() {

        List<Stable> stables = new ArrayList<>(
                List.of(
                        new Stable("StblHn", 5),
                        new Stable("PonyCo", 3)
                )
        );

        stableRepository.saveAll(stables);
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

        List<Stable> stables = stableRepository.findAll();

        animals.get(1).setStable(stables.getFirst());
        animals.get(2).setStable(stables.getLast());

        animalRepository.saveAll(animals);
    }
}

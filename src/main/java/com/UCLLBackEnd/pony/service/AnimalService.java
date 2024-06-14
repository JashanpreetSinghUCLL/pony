package com.UCLLBackEnd.pony.service;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    private AnimalRepository animalRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal addAnimal(Animal animal) {

        if (animalRepository.findByName(animal.getName()).isPresent()) {
            throw new ServiceException("ServiceException", "Animal already exists");
        }

        return animalRepository.save(animal);
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public List<Animal> getAllAnimalsOlderThan(int age) {

        if (age < 0) {
            throw new ServiceException("ServiceException", "Age cannot be negative");
        }

        if (age < 1 || age > 50) {
            throw new ServiceException("ServiceException", "Age must be between 1 and 50");
        }

        return animalRepository.findByAgeGreaterThan(age);
    }

    public Animal getOldestAnimal() {
        return animalRepository.findOldestAnimals();
    }
}

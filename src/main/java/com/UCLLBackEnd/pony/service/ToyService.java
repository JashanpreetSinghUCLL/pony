package com.UCLLBackEnd.pony.service;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.model.Toy;
import com.UCLLBackEnd.pony.repository.AnimalRepository;
import com.UCLLBackEnd.pony.repository.ToyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ToyService {

    private ToyRepository toyRepository;

    private AnimalRepository animalRepository;

    @Autowired
    public ToyService(ToyRepository toyRepository, AnimalRepository animalRepository) {
        this.toyRepository = toyRepository;
        this.animalRepository = animalRepository;
    }

    public Toy addToy(Toy toy) {
        return toyRepository.save(toy);
    }

    public Toy addAnimalToToy(Long animalId, Long toyId) {
        Toy toy = toyRepository.findById(toyId)
                .orElseThrow(() -> new ServiceException("ServiceException", "Toy with " + toyId + " does not exist"));

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new ServiceException("ServiceException", "Animal with " + animalId + " does not exist"));

        toy.setAnimals(new HashSet<>(Set.of(animal)));
        animal.setToys(new HashSet<>(Set.of(toy)));

        return toyRepository.save(toy);
    }

    public Toy getToyWithSpecificName(String toyName) {

        if (toyRepository.findByName(toyName) == null) {
            throw new ServiceException("ServiceException", "Toy with " + toyName + " does not exist");
        }

        return toyRepository.findByName(toyName);
    }
}

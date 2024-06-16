package com.UCLLBackEnd.pony.service;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.model.Stable;
import com.UCLLBackEnd.pony.repository.AnimalRepository;
import com.UCLLBackEnd.pony.repository.StableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StableService {

    private AnimalRepository animalRepository;
    private StableRepository stableRepository;

    @Autowired
    public StableService(StableRepository stableRepository, AnimalRepository animalRepository) {
        this.stableRepository = stableRepository;
        this.animalRepository = animalRepository;
    }

    public Stable assignAnimalToNewStable(String animalName, Stable stable) {

        Animal animal = animalRepository.findByName(animalName)
                .orElseThrow(() -> new ServiceException("ServiceException", "Animal doesn't exist"));

        if (stable == null) {
            throw new ServiceException("ServiceException", "Stable not given");
        }

        if (stableRepository.existsByName(stable.getName())) {
            throw new ServiceException("ServiceException", "Stable already exists");
        }

        if (stable.getMax_number_of_animals() == 0) {
            throw new ServiceException("ServiceException", "No animals can be assigned because of max number of animals is 0");
        }

        stableRepository.save(stable);
        animal.setStable(stable);
        stable.setAnimals(new HashSet<>(Set.of(animal)));
        animalRepository.save(animal);
        return stableRepository.save(stable);
    }

    public Stable assignAnimalToExistingStable(String animalName, Long stableId) {

        Animal animal = animalRepository.findByName(animalName)
                .orElseThrow(() -> new ServiceException("ServiceException", "Animal doesn't exist"));

        Stable stable = stableRepository.findById(stableId).orElseThrow(() -> new ServiceException("ServiceException", "Stable with id " + stableId + " doesn't exist"));

        List<Animal> numberOfAnimals = animalRepository.findByStableWithId(stableId);

        if (stable.getMax_number_of_animals() == 0) {
            throw new ServiceException("ServiceException", "No animals can be assigned to this stable, It's maxed out of animals");
        }


        if (numberOfAnimals.size() >= stable.getMax_number_of_animals()) {
            throw new ServiceException("ServiceException", "Max number of animals reached");
        }
        animal.setStable(stable);
        animalRepository.save(animal);
        return stableRepository.save(stable);
    }

    public List<Stable> getAllStablesWithAnimalsSleepingInThem() {

        return stableRepository
                .findAll()
                .stream()
                .filter(s -> !s.getAnimals().isEmpty())
                .toList();
    }

    public Stable getStableOfAnimal(String animalName) {

        Animal animal = animalRepository.findByName(animalName)
                .orElseThrow(() -> new ServiceException("ServiceException", "The given animal doesn't exists"));

        return animal.getStable();
    }


    public List<Stable> getAllStablesWithPlacesLeft() {

        return stableRepository.findAll()
                .stream()
                .filter(s -> s.getAnimals().size() < s.getMax_number_of_animals())
                .collect(Collectors.toList());
    }

    public Stable getStableWithTheMostAnimals() {

        return stableRepository.findAll()
                .stream()
                .max(Comparator.comparing(stable -> stable.getAnimals().size()))
                .orElseThrow(() -> new ServiceException("ServiceException", "No stable with animals sleeping in them found"));

    }

    public List<Stable> getAllStablesOfOwner(String ownerName) {
        return stableRepository.findAllStablesOfOwner(ownerName);
    }
}

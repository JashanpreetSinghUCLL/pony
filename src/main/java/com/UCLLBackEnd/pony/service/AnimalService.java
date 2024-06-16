package com.UCLLBackEnd.pony.service;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.model.Chicken;
import com.UCLLBackEnd.pony.model.MedicalRecord;
import com.UCLLBackEnd.pony.model.Pony;
import com.UCLLBackEnd.pony.repository.AnimalRepository;
import com.UCLLBackEnd.pony.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private AnimalRepository animalRepository;

    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository, MedicalRecordRepository medicalRecordRepository) {
        this.animalRepository = animalRepository;
        this.medicalRecordRepository = medicalRecordRepository;
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

    public Animal addNewMedicalRecordToExistingAnimal(Long animalId, MedicalRecord medicalRecord) {

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new ServiceException("ServiceException", "Animal with id " + animalId + " does not exist"));

        animal.setMedicalRecords(new HashSet<>(Set.of(medicalRecord)));
        medicalRecord.setAnimal(animal);
        medicalRecordRepository.save(medicalRecord);
        return animalRepository.save(animal);
    }

    public List<Animal> getAnimalWithOutClosingDate() {

        List<Animal> animals = getAllAnimals();

        return animals.stream()
                .filter(a -> a.getMedicalRecords().stream()
                .anyMatch(medicalRecord -> medicalRecord.getClosingDate() == null))
                .collect(Collectors.toList());
    }

    public Pony addPony(Pony pony) {

        return animalRepository.save(pony);
    }

    public Chicken addChicken(Chicken chicken) {

        return animalRepository.save(chicken);
    }

    public List<Animal> getAllChickens() {
        return animalRepository.findAll()
                .stream()
                .filter(a -> Objects.equals(a.getAnimal_type(), "CHICKEN"))
                .collect(Collectors.toList());
    }

    public List<Animal> getAllPonies() {
        return animalRepository.findAll()
                .stream()
                .filter(a -> Objects.equals(a.getAnimal_type(), "PONY"))
                .collect(Collectors.toList());
    }

    public List<Animal> getPoniesOlderThanGivenAge(int age) {

        if (age < 0 || age > 50) {
            throw new ServiceException("ServiceException", "Age must be between 1 and 50");
        }

        return animalRepository.findByAgeGreaterThan(age)
                .stream()
                .filter(a -> Objects.equals(a.getAnimal_type(), "PONY"))
                .collect(Collectors.toList());

    }

    public List<Animal> getAllChickensWhoLaysEggs() {

        return animalRepository.findAllChickensWhoLaysEggs();
    }
}

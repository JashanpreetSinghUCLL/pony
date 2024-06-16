package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DbInitializer {

    private MedicalRecordRepository medicalRecordRepository;

    private AnimalRepository animalRepository;

    private StableRepository stableRepository;

    private AddressRepository addressRepository;

    private ToyRepository toyRepository;

    @Autowired
    public DbInitializer(AnimalRepository animalRepository, StableRepository stableRepository, AddressRepository addressRepository, ToyRepository toyRepository, MedicalRecordRepository medicalRecordRepository) {
        this.animalRepository = animalRepository;
        this.stableRepository = stableRepository;
        this.addressRepository = addressRepository;
        this.toyRepository = toyRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @PostConstruct
    public void init() {
        addToys();
        addAddresses();
        addStables();
        addAnimals();
        addMedicalRecords();
    }

    private void addMedicalRecords() {

        List<MedicalRecord> medicalRecords = new ArrayList<>(
                List.of(
                        new MedicalRecord(LocalDate.of(2024, 3, 10), "Ate too much bad food"),
                        new MedicalRecord(LocalDate.of(2024, 5, 1), "Sick")
                )
        );

        List<Animal> animals = animalRepository.findAll();

        medicalRecords.getFirst().setAnimal(animals.get(1));
        medicalRecords.getLast().setAnimal(animals.get(1));
        medicalRecords.getLast().setClosingDate(LocalDate.of(2024, 5, 20));

        medicalRecordRepository.saveAll(medicalRecords);
    }

    private void addToys() {

        List<Toy> toys = new ArrayList<>(
                List.of(
                        new Toy("Toytoy"),
                        new Toy("Nerf Machine Gun")
                )
        );

        toyRepository.saveAll(toys);
    }

    private void addAddresses() {

        List<Address> addresses = new ArrayList<>(
                List.of(
                        new Address("Hello Road", 1002, "Birmingham"),
                        new Address("Via Roma", 11, "Milan")
                )
        );

        for (Address address : addresses) {
            addressRepository.save(address);
        }
    }

    private void addStables() {

        List<Stable> stables = new ArrayList<>(
                List.of(
                        new Stable("StblHn", 5),
                        new Stable("PonyCo Italia", 5)
                )
        );

        List<Address> addresses = addressRepository.findAll();

        stables.getFirst().setAddress(addresses.getFirst());
        stables.getLast().setAddress(addresses.getLast());

        stableRepository.saveAll(stables);
    }

    private void addAnimals() {

        List<Animal> animals = new ArrayList<>(
                List.of(
                        new Animal("Bella", 20),
                        new Animal("Luna", 10),
                        new Animal("Muriel", 2),
                        new Animal("Little", 1),
                        new Animal("Breda", 32),
                        new Animal("Deby", 13),
                        new Animal("Henry", 32)
                )
        );

        List<Stable> stables = stableRepository.findAll();

        List<Toy> toys = toyRepository.findAll();

        animals.get(1).setStable(stables.getFirst());
        animals.get(2).setStable(stables.getLast());
        animals.get(3).setStable(stables.getLast());
        animals.get(4).setStable(stables.getLast());
        animals.get(5).setStable(stables.getLast());
        animals.get(6).setStable(stables.getLast());

        animals.get(3).setToys(Set.of(toys.get(0), toys.get(1)));
        animals.get(5).setToys(Set.of(toys.get(0)));

        animalRepository.saveAll(animals);
    }
}

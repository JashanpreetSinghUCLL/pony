package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.Address;
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

    private AddressRepository addressRepository;

    @Autowired
    public DbInitializer(AnimalRepository animalRepository, StableRepository stableRepository, AddressRepository addressRepository) {
        this.animalRepository = animalRepository;
        this.stableRepository = stableRepository;
        this.addressRepository = addressRepository;
    }

    @PostConstruct
    public void init() {
        addAddresses();
        addStables();
        addAnimals();
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

        animals.get(1).setStable(stables.getFirst());
        animals.get(2).setStable(stables.getLast());
        animals.get(3).setStable(stables.getLast());
        animals.get(4).setStable(stables.getLast());
        animals.get(5).setStable(stables.getLast());
        animals.get(6).setStable(stables.getLast());

        animalRepository.saveAll(animals);
    }
}

package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Animal> findByName(String name);

    List<Animal> findByAgeGreaterThan(int age);

    @Query("SELECT a FROM Animal a ORDER BY a.age DESC LIMIT 1")
    Animal findOldestAnimals();
}

package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Animal> findByName(String name);

    List<Animal> findByAgeGreaterThan(int age);

    @Query("SELECT a FROM Animal a ORDER BY a.age DESC LIMIT 1")
    Animal findOldestAnimals();

    @Query("SELECT a FROM Animal a WHERE a.id = :stableId")
    List<Animal> findByStableWithId(@Param("stableId") Long stableId);

    @Query("SELECT c FROM Chicken c WHERE c.laysEggs = true")
    List<Animal> findAllChickensWhoLaysEggs();
}

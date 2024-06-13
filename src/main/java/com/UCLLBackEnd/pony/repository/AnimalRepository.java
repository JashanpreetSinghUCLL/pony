package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Animal> findByName(String name);

}

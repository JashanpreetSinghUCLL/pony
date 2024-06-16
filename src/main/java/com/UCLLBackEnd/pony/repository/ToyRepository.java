package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.Toy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToyRepository extends JpaRepository<Toy, Long> {

    Toy findByName(String toyName);
}

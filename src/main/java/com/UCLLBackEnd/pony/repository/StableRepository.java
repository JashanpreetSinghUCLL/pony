package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.Stable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StableRepository extends JpaRepository<Stable, Long> {

    boolean existsByName(String name);

    List<Stable> findByName(String name);
}

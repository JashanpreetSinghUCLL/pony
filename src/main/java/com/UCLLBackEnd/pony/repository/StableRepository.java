package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.Stable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StableRepository extends JpaRepository<Stable, Long> {

    boolean existsByName(String name);

    List<Stable> findByName(String name);

    @Query("SELECT s from Stable s WHERE s.owner = :ownerName")
    List<Stable> findAllStablesOfOwner(@Param("ownerName") String ownerName);


}

package com.UCLLBackEnd.pony.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@Entity
@Table(name = "toys")
public class Toy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is required")
    private String name;

    @ManyToMany(mappedBy = "toys")
    Set<Animal> animals;

    public Toy(String name) {
        this.name = name;
    }

    public Toy() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(Set<Animal> animals) {
        this.animals = animals;
    }
}

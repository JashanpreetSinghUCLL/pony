package com.UCLLBackEnd.pony.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "stables")
public class Stable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The name of stable is required")
    private String name;

    @PositiveOrZero(message = "Max number of animals should be 0 or more")
    private int max_number_of_animals;

    public Stable(String name, int max_number_of_animals) {
        this.name = name;
        this.max_number_of_animals = max_number_of_animals;
    }

    public Stable() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMax_number_of_animals() {
        return max_number_of_animals;
    }

    public void setMax_number_of_animals(int max_number_of_animals) {
        this.max_number_of_animals = max_number_of_animals;
    }
}
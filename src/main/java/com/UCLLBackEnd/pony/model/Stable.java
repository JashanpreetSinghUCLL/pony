package com.UCLLBackEnd.pony.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Set;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "stable")
    private Set<Animal> animals;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

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

    public Set<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(Set<Animal> animals) {
        this.animals = animals;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Stable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", max_number_of_animals=" + max_number_of_animals +
                ", animals=" + animals +
                ", address=" + address +
                '}';
    }
}

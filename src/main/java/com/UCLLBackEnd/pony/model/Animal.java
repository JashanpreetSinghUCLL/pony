package com.UCLLBackEnd.pony.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@Entity
@Table(name="animals")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "animal_type")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Name is required.")
    @Column(unique = true)
    private String name;

    @Min(value = 0, message = "Minimum age is 0")
    @Max(value = 50, message = "Maximum age is 50")
    private int age;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="stable_id")
    private Stable stable;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "animals_toys",
            joinColumns = @JoinColumn(name = "animal_id"),
            inverseJoinColumns = @JoinColumn(name = "toy_id"))
    Set<Toy> toys;

    @JsonManagedReference
    @OneToMany(mappedBy = "animal")
    private Set<MedicalRecord> medicalRecords;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Animal(String name) {
    }

    public Animal() {

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Stable getStable() {
        return stable;
    }

    public void setStable(Stable stable) {
        this.stable = stable;
    }

    public Set<Toy> getToys() {
        return toys;
    }

    public void setToys(Set<Toy> toys) {
        this.toys = toys;
    }

    public Set<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(Set<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    @Column(insertable = false, updatable = false)
    private String animal_type;

    public String getAnimal_type() {
        return animal_type;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", stable=" + stable +
                ", toys=" + toys +
                '}';
    }
}

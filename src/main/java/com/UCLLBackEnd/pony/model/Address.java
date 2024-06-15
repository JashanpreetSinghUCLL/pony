package com.UCLLBackEnd.pony.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Street must be given")
    private String street;

    @Positive(message = "Number must be atleast 1")
    private int number;

    @NotEmpty(message = "The place must be given")
    private String place;

    public Address(String street, int number, String place) {
        this.street = street;
        this.number = number;
        this.place = place;
    }

    public Address() {

    }

    public Address(long id, String street, int number, String place) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.place = place;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", place='" + place + '\'' +
                '}';
    }
}

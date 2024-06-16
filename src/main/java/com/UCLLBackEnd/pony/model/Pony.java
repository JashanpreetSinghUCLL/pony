package com.UCLLBackEnd.pony.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PONY")
public class Pony extends Animal{

    public Pony(int age) {
        super("Pony", age);
    }

    public Pony(String name, int age) {
        super(name, age);
    }

    public Pony() {
        super();
    }
}

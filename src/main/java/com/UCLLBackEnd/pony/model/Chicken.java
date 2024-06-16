package com.UCLLBackEnd.pony.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CHICKEN")
public class Chicken extends Animal{

    @Column(name = "lays_eggs")
    private boolean laysEggs;

    public Chicken(String name, int age, boolean laysEggs) {
        super(name, age);
        this.laysEggs = laysEggs;
    }

    public Chicken(boolean laysEggs) {
        this.laysEggs = laysEggs;
    }

    public Chicken() {
        super();
    }

    public boolean isLaysEggs() {
        return laysEggs;
    }

    public void setLaysEggs(boolean laysEggs) {
        this.laysEggs = laysEggs;
    }
}

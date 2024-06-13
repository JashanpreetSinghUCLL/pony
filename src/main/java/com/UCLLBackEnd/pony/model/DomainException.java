package com.UCLLBackEnd.pony.model;

public class DomainException extends RuntimeException {

    private String field;

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

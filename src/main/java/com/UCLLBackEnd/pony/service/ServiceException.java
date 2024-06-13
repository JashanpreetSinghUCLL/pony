package com.UCLLBackEnd.pony.service;

public class ServiceException extends RuntimeException {

    private String field;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

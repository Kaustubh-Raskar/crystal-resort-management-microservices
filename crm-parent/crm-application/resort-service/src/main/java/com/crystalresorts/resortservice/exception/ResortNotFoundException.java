package com.crystalresorts.resortservice.exception;

public class ResortNotFoundException extends RuntimeException {

    public ResortNotFoundException(Long id) {
        super("Resort not found with id: " + id);
    }
}
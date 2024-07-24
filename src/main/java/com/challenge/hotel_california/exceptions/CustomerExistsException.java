package com.challenge.hotel_california.exceptions;

public class CustomerExistsException extends RuntimeException {
    public CustomerExistsException(String message) {
        super(message);
    }
}

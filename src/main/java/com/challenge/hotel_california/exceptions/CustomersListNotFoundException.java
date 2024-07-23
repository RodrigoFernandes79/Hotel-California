package com.challenge.hotel_california.exceptions;

public class CustomersListNotFoundException extends RuntimeException {
    public CustomersListNotFoundException(String message) {
        super(message);
    }
}

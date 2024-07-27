package com.challenge.hotel_california.exceptions;

public class BookingsNotFoundException extends RuntimeException {
    public BookingsNotFoundException(String message) {
        super(message);
    }
}

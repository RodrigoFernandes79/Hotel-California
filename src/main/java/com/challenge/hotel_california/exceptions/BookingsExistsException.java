package com.challenge.hotel_california.exceptions;

public class BookingsExistsException extends RuntimeException {
    public BookingsExistsException(String message) {
        super(message);
    }
}

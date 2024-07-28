package com.challenge.hotel_california.exceptions;

public class BookingCheckInDateNotBeforeException extends RuntimeException {
    public BookingCheckInDateNotBeforeException(String message) {
        super(message);
    }
}

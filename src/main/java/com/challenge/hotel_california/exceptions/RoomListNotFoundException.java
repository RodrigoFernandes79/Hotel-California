package com.challenge.hotel_california.exceptions;

public class RoomListNotFoundException extends RuntimeException {
    public RoomListNotFoundException(String message) {
        super(message);
    }
}

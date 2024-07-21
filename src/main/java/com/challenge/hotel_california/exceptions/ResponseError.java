package com.challenge.hotel_california.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ResponseError(
        String message,
        HttpStatus httpStatus,
        LocalDateTime time
) {
}

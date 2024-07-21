package com.challenge.hotel_california.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ResponseError> roomNotFoundException(RoomNotFoundException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RoomNotAvailableException.class)
    public ResponseEntity<ResponseError> roomNotAvailableException(RoomNotAvailableException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BookingsExistsException.class)
    public ResponseEntity<ResponseError> BookingsExistsException(BookingsExistsException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RoomListNotFoundException.class)
    public ResponseEntity<ResponseError> BookingsExistsException(RoomListNotFoundException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

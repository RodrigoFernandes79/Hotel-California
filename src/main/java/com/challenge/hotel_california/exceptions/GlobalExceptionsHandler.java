package com.challenge.hotel_california.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

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
    public ResponseEntity<ResponseError> bookingsExistsException(BookingsExistsException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RoomListNotFoundException.class)
    public ResponseEntity<ResponseError> roomListNotFoundException(RoomListNotFoundException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NumberRoomFoundException.class)
    public ResponseEntity<ResponseError> numberRoomFoundException(NumberRoomFoundException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DataFieldValidation>> dataField(MethodArgumentNotValidException e) {
        var errors = e.getFieldErrors();
        var error = errors.stream().map(DataFieldValidation::new).toList();
        return ResponseEntity.badRequest().body(error);
    }

    private record DataFieldValidation(String error, String message) {
        private DataFieldValidation(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    @ExceptionHandler(CustomersListNotFoundException.class)
    public ResponseEntity<ResponseError> customersListNotFoundException(CustomersListNotFoundException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CustomerExistsException.class)
    public ResponseEntity<ResponseError> customerExistsException(CustomerExistsException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ResponseError> customerNotFoundException(CustomerNotFoundException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BookingsNotFoundException.class)
    public ResponseEntity<ResponseError> customerNotFoundException(BookingsNotFoundException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

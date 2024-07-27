package com.challenge.hotel_california.controller;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.DTOs.BookingOutputDTO;
import com.challenge.hotel_california.DTOs.BookingOutputListDTO;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.service.BookingService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    @Transactional
    public ResponseEntity<BookingOutputDTO> createANewReservation(@Valid @RequestBody BookingEntryDTO bookingEntryDTO,
                                                                  UriComponentsBuilder uriComponentsBuilder) {
        Booking booking = bookingService.createAReservation(bookingEntryDTO);
        URI uri = uriComponentsBuilder.path("/bookings/{id}")
                .buildAndExpand(booking.getId()).toUri();

        return ResponseEntity.created(uri).body(new BookingOutputDTO(booking));

    }

    @GetMapping
    public ResponseEntity<Page<BookingOutputListDTO>> listAllReservations(@PageableDefault(sort = {"customerName"}, size = 5) Pageable pageable) {
        return bookingService.listAllReservations(pageable);

    }

    @GetMapping("/customer_name")
    public ResponseEntity<Page<BookingOutputListDTO>> listAllReservationsByCustomer(@PageableDefault(sort = {"customerName"}, size = 5) Pageable pageable,
                                                                                      @RequestParam("customerName") String customerName) {
        return bookingService.listAllReservationsByCustomer(pageable, customerName);
    }
}

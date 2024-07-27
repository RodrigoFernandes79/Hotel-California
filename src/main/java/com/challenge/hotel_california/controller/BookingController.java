package com.challenge.hotel_california.controller;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.DTOs.BookingOutputDTO;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.service.BookingService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}

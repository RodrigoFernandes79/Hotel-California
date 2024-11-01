package com.challenge.hotel_california.controller;

import com.challenge.hotel_california.DTOs.*;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.HashMap;
import java.util.Map;

@Tag(name = "Booking", description = "Endpoints for Managing Booking")
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Operation(
            summary = "Create a new Reservation", description = "Creates a new reservation with the provided details",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = BookingOutputDTO.class))),
                    @ApiResponse(description = "Bad Request",
                            responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error",
                            responseCode = "500", content = @Content)
            }
    )
    @PostMapping
    @Transactional
    public ResponseEntity<BookingOutputDTO> createANewReservation(@Valid @RequestBody BookingEntryDTO bookingEntryDTO,
                                                                  UriComponentsBuilder uriComponentsBuilder) {
        Booking booking = bookingService.createAReservation(bookingEntryDTO);
        URI uri = uriComponentsBuilder.path("/bookings/{id}")
                .buildAndExpand(booking.getId()).toUri();

        return ResponseEntity.created(uri).body(new BookingOutputDTO(booking));

    }

    @Operation(summary = "Get Bookings List paginated", description = "Get a list of all the bookings created with pagination feature",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookingOutputListDTO.class)))),
                    @ApiResponse(description = "No Content", responseCode = "204",
                            content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500",
                            content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<Page<BookingOutputListDTO>> listAllReservations(@PageableDefault(sort = {"customerName"}, size = 5) Pageable pageable) {
        bookingService.listAllReservations(pageable);
        return ResponseEntity.ok().body(bookingService.listAllReservations(pageable));

    }

    @Operation(
            summary = "Get Reservations by Customer Name",
            description = "Retrieve all reservations for a specific customer by their name",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookingOutputListDTO.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400",
                            content = @Content)
            }
    )
    @GetMapping("/customer_name")
    public ResponseEntity<Page<BookingOutputListDTO>> listAllReservationsByCustomer(@PageableDefault(sort = {"customerName"}, size = 5) Pageable pageable,
                                                                                    @RequestParam("customerName") String customerName) {

        return ResponseEntity.ok().body(bookingService.listAllReservationsByCustomer(pageable, customerName));
    }

    @Operation(
            summary = "Update an existing Reservation",
            description = "Update the details of an existing reservation",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BookingOutputDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content)
            }
    )
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<BookingOutputDTO> updateReservation(@Valid @RequestBody BookingUpdateEntryDTO bookingUpdateEntryDTO, @PathVariable Long id) {
        return ResponseEntity.ok().body(bookingService.updateReservation(bookingUpdateEntryDTO, id));
    }

    @Operation(
            summary = "Delete a Reservation",
            description = "Remove an existing reservation by ID",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BookingDeleteStatusDTO.class))),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<BookingDeleteStatusDTO> deleteAReservation(@PathVariable Long id) {
        return ResponseEntity.ok().body(bookingService.deleteAReservation(id));
    }

    @Operation(
            summary = "Update Check-Out Date",
            description = "Update the check-out date for a reservation",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content)
            }
    )
    @PatchMapping("/checkout/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> updateCheckOutDate(@PathVariable Long id) {
        bookingService.updateCheckOutDate(id);
        Map<String, String> message = new HashMap<>();
        message.put("message: ", "Checkout successful!");
        return ResponseEntity.ok().body(message);

    }
}

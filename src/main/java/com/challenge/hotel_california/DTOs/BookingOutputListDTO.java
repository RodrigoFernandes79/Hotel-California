package com.challenge.hotel_california.DTOs;

import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.model.Booking;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingOutputListDTO(
        Long booking_id,
        String customer,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime checkInDate,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime checkOutDate,
        BookingStatus statusBooking,
        String roomNumber,
        int daily,
        BigDecimal totalPrice
) {
    public BookingOutputListDTO(Booking booking) {
        this(booking.getId(), booking.getCustomerName().getName(),
                booking.getCheckInDate(), booking.getCheckOutDate(), booking.getStatus(),
                booking.getRoom().getNumber(), booking.getDaily(), booking.getTotalPrice());
    }
}

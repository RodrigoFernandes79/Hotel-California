package com.challenge.hotel_california.DTOs;

import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.model.Booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingDeleteStatusDTO(
        Long id,
        String customerName,
        LocalDateTime checkInDate,
        BookingStatus bookingStatus,
        String roomNumber,
        int daily,
        BigDecimal totalPrice
) {
    public BookingDeleteStatusDTO(Booking booking) {
        this(booking.getId(), booking.getCustomerName().getName(),
                booking.getCheckInDate(), booking.getStatus(), booking.getRoom().getNumber(),
                booking.getDaily(), booking.getTotalPrice());
    }
}

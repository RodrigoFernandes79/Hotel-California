package com.challenge.hotel_california.DTOs;

import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.enums.RoomType;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Room;

import java.math.BigDecimal;
import java.util.List;

public record RoomGetByIdDTO(
        Long id,
        String number,
        BigDecimal price,
        RoomType type,
        RoomStatus status,
        List<Booking> bookings
) {
    public RoomGetByIdDTO(Room room) {
        this(room.getId(), room.getNumber(), room.getPrice(), room.getType(),
                room.getStatus(), room.getBookings());
    }
}
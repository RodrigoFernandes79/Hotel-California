package com.challenge.hotel_california.DTOs;

import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.enums.RoomType;

import java.math.BigDecimal;


public record RoomEntryDTO(
        String number,
        BigDecimal price,
        RoomType type,
        RoomStatus status
) {
    public RoomEntryDTO(String number, BigDecimal price, RoomType type, RoomStatus status) {
        this.number = number;
        this.price = price;
        this.type = type;
        this.status = RoomStatus.AVAILABLE;
    }
}

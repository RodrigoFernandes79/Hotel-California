package com.challenge.hotel_california.DTOs;

import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.enums.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record RoomEntryDTO(
        @NotBlank(message = "{number.required}")
        String number,
        @NotNull(message = "{price.required}")
        BigDecimal price,
        @NotNull(message = "{type.required}")
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

package com.challenge.hotel_california.DTOs;

import com.challenge.hotel_california.enums.RoomType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record RoomEntryUpdateDTO(
        @NotNull(message = "{id.format}")
        Long id,
        String number,

        BigDecimal price,

        RoomType type
) {

}

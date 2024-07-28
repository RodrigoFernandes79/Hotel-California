package com.challenge.hotel_california.DTOs;

import com.challenge.hotel_california.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookingUpdateEntryDTO(
        @NotNull(message = "{id.format}")
        Long id,
        Long customerId,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime checkInDate,
        BookingStatus status,
        Long roomId
) {
}

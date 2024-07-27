package com.challenge.hotel_california.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookingEntryDTO(
        @NotNull(message = "{id.format}")
        Long customerId,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        @FutureOrPresent(message = "{check.format}")
        LocalDateTime checkInDate,
        @NotNull(message = "{id.format}")
        Long roomId
) {
}

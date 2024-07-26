package com.challenge.hotel_california.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CustomerUpdateEntryDTO(
        @NotNull(message = "{id.format}")
        Long id,
        String name,

        @Email(message = "{email.format}")
        String email,

        @Length(max = 11, message = "{phone.length}")
        String phone
) {

}

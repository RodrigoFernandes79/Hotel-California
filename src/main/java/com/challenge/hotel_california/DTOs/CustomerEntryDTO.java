package com.challenge.hotel_california.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CustomerEntryDTO(
        @NotBlank(message = "{name.required}")
        String name,
        @NotBlank(message = "{email.required}")
        @Email(message = "{email.format}")
        String email,
        @NotBlank(message = "{phone.required}")
        @Length(max = 11, message = "{phone.length}")
        String phone
) {

}

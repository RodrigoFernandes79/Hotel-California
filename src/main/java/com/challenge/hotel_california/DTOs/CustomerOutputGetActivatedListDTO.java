package com.challenge.hotel_california.DTOs;

import com.challenge.hotel_california.model.Customer;

public record CustomerOutputGetActivatedListDTO(
        Long id,
        String name,
        String email,
        String phone,
        Boolean isDeleted
) {
    public CustomerOutputGetActivatedListDTO(Customer activatedCustomer) {
        this(activatedCustomer.getId(), activatedCustomer.getName(), activatedCustomer.getEmail(), activatedCustomer.getPhone(), activatedCustomer.getIsDeleted());
    }

}

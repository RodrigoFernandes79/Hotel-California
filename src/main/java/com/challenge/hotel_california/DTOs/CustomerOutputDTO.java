package com.challenge.hotel_california.DTOs;

import com.challenge.hotel_california.model.Customer;

public record CustomerOutputDTO(
        Long id,
        String name,
        String email,
        String phone
) {
    public CustomerOutputDTO(Customer customer) {
        this(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone());
    }
}

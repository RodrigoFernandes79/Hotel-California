package com.challenge.hotel_california.DTOs;

import com.challenge.hotel_california.model.Customer;

public record CustomerOutputGetListDTO(
        String name,
        String email,
        String phone
) {
    public CustomerOutputGetListDTO(Customer customer) {
        this(customer.getName(), customer.getEmail(), customer.getPhone());
    }
}

package com.challenge.hotel_california.DTOs;

import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;

import java.util.List;

public record CustomerGetByIdDTO(
        Long id,
        String name,
        String email,
        String phone,
        List<Booking> bookings
) {
    public CustomerGetByIdDTO(Customer customer) {
        this(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone(), customer.getBookings());
    }
}

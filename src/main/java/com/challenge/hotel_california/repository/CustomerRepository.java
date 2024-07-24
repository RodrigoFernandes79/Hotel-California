package com.challenge.hotel_california.repository;

import com.challenge.hotel_california.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional findByName(String name);

    Optional findByEmail(String email);

    Optional findByPhone(String phone);
}

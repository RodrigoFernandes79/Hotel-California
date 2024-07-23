package com.challenge.hotel_california.repository;

import com.challenge.hotel_california.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

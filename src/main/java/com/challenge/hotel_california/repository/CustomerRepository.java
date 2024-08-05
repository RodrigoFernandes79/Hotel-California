package com.challenge.hotel_california.repository;

import com.challenge.hotel_california.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByName(String name);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhone(String phone);

    Page<Customer> findByIsDeletedIsFalse(Pageable pageable);

    List<Customer> findByNameContainingIgnoreCase(String customerName);
}

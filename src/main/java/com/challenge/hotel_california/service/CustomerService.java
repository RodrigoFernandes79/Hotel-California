package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.CustomerOutputGetListDTO;
import com.challenge.hotel_california.exceptions.CustomersListNotFoundException;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Page<CustomerOutputGetListDTO> lisAllCustomers(Pageable pageable) {
        Page<Customer> findCustomers = customerRepository.findAll(pageable);
        if (findCustomers.isEmpty()) {
            throw new CustomersListNotFoundException("No customers found into Database!");
        }
        return findCustomers.map(CustomerOutputGetListDTO::new);
    }
}

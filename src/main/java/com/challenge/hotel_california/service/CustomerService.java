package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.CustomerEntryDTO;
import com.challenge.hotel_california.DTOs.CustomerOutputGetListDTO;
import com.challenge.hotel_california.exceptions.CustomerExistsException;
import com.challenge.hotel_california.exceptions.CustomersListNotFoundException;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Customer addCustomer(CustomerEntryDTO customerEntryDTO) {
        Optional foundCustomerName = customerRepository.findByName(customerEntryDTO.name());
        if (foundCustomerName.isPresent()) {
            throw new CustomerExistsException("Customer " + customerEntryDTO.name() + " already exists in Database!");
        }
        Optional foundCustomerEmail = customerRepository.findByEmail(customerEntryDTO.email());
        if (foundCustomerEmail.isPresent()) {
            throw new CustomerExistsException("Email " + customerEntryDTO.email() + " already exists in Database!");
        }
        Optional foundCustomerPhone = customerRepository.findByPhone(customerEntryDTO.phone());
        if (foundCustomerPhone.isPresent()) {
            throw new CustomerExistsException("Phone " + customerEntryDTO.phone() + " already exists in Database!");
        }
        return customerRepository.save(new Customer(customerEntryDTO));
    }
}

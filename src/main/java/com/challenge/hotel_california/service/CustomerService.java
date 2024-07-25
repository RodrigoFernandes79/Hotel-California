package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.CustomerEntryDTO;
import com.challenge.hotel_california.DTOs.CustomerGetByIdDTO;
import com.challenge.hotel_california.DTOs.CustomerOutputGetListDTO;
import com.challenge.hotel_california.exceptions.CustomerNotFoundException;
import com.challenge.hotel_california.exceptions.CustomersListNotFoundException;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.repository.CustomerRepository;
import com.challenge.hotel_california.validatorRefactor.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private List<IValidator> verifyValidators;

    public Page<CustomerOutputGetListDTO> lisAllCustomers(Pageable pageable) {
        Page<Customer> findCustomers = customerRepository.findAll(pageable);
        if (findCustomers.isEmpty()) {
            throw new CustomersListNotFoundException("No customers found into Database!");
        }
        return findCustomers.map(CustomerOutputGetListDTO::new);
    }

    public Customer addCustomer(CustomerEntryDTO customerEntryDTO) {
        verifyValidators.forEach(v -> v.verifyValidators(customerEntryDTO));

        return customerRepository.save(new Customer(customerEntryDTO));
    }

    public CustomerGetByIdDTO getDetailsOfASpecificCustomer(Long id) {
        Customer customer = customerRepository.getReferenceById(id);
        if (!customerRepository.existsById(customer.getId())) {
            throw new CustomerNotFoundException("Customer " + id + " not found!");
        }
        return new CustomerGetByIdDTO(customer);
    }
}

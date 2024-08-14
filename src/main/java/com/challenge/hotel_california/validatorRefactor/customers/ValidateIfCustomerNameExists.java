package com.challenge.hotel_california.validatorRefactor.customers;

import com.challenge.hotel_california.DTOs.CustomerEntryDTO;
import com.challenge.hotel_california.exceptions.CustomerExistsException;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateIfCustomerNameExists implements IValidatorCustomers {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void verifyCustomersValidators(CustomerEntryDTO customerEntryDTO) {
        Optional<Customer> foundCustomerName = customerRepository.findByName(customerEntryDTO.name());
        if (foundCustomerName.isPresent()) {
            throw new CustomerExistsException("Customer " + customerEntryDTO.name() + " already exists in Database!");
        }
    }
}

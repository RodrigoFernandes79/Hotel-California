package com.challenge.hotel_california.validatorRefactor;

import com.challenge.hotel_california.DTOs.CustomerEntryDTO;
import com.challenge.hotel_california.exceptions.CustomerExistsException;
import com.challenge.hotel_california.exceptions.CustomerNotFoundException;
import com.challenge.hotel_california.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidatorCustomerExists implements IValidator {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void verifyValidators(CustomerEntryDTO customerEntryDTO) {
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
    }
}

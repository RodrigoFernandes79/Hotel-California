package com.challenge.hotel_california.validatorRefactor.customers;

import com.challenge.hotel_california.DTOs.CustomerEntryDTO;
import com.challenge.hotel_california.exceptions.CustomerExistsException;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ValidateIfCustomerNameExistsTest {
    @InjectMocks
    private ValidateIfCustomerNameExists validateIfCustomerNameExists;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private Optional<Customer> foundCustomerName;
    @Mock
    private CustomerEntryDTO customerEntryDTO;

    @Test
    @DisplayName("Verify if customer name already exists and throws exception")
    void verifyCustomersValidatorsScenario01() {
        //Arrange
        BDDMockito.given(customerRepository.findByName(customerEntryDTO.name())).willReturn(foundCustomerName);
        BDDMockito.given(foundCustomerName.isPresent()).willReturn(true);
        //Act & Assert
        Assertions.assertThrows(CustomerExistsException.class, () -> validateIfCustomerNameExists
                .verifyCustomersValidators(customerEntryDTO));

    }

    @Test
    @DisplayName("Verify if customer name not exists and not throws exception")
    void verifyCustomersValidatorsScenario02() {
        //Arrange
        BDDMockito.given(customerRepository.findByName(customerEntryDTO.name())).willReturn(Optional.empty());

        //Act & Assert
        Assertions.assertDoesNotThrow(() -> validateIfCustomerNameExists
                .verifyCustomersValidators(customerEntryDTO));

    }
}
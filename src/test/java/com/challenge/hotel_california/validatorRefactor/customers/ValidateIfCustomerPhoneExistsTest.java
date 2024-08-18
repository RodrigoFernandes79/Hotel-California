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
class ValidateIfCustomerPhoneExistsTest {
    @InjectMocks
    private ValidateIfCustomerPhoneExists validateIfCustomerPhoneExists;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerEntryDTO customerEntryDTO;
    @Mock
    private Optional<Customer> foundCustomerPhone;

    @Test
    @DisplayName("Verify if customer phone already exists and throws exception")
    void verifyCustomersValidatorsScenario01() {
        //Arrange
        BDDMockito.given(customerRepository.findByPhone(customerEntryDTO.phone())).willReturn(foundCustomerPhone);
        BDDMockito.given(foundCustomerPhone.isPresent()).willReturn(true);
        //Act & Arrange
        Assertions.assertThrows(CustomerExistsException.class, () -> validateIfCustomerPhoneExists
                .verifyCustomersValidators(customerEntryDTO));
    }

    @Test
    @DisplayName("Verify if customer phone not exists and not throws exception")
    void verifyCustomersValidatorsScenario02() {
        //Arrange
        BDDMockito.given(customerRepository.findByPhone(customerEntryDTO.phone())).willReturn(Optional.empty());

        //Act & Arrange
        Assertions.assertDoesNotThrow(() -> validateIfCustomerPhoneExists
                .verifyCustomersValidators(customerEntryDTO));
    }
}
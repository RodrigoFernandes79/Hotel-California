package com.challenge.hotel_california.validatorRefactor.bookingsCreateValidation;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.exceptions.CustomerNotFoundException;
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

@ExtendWith(MockitoExtension.class)
class ValidateIfUpdateCustomerExistsOrDeletedTest {
    @InjectMocks
    private ValidateIfUpdateCustomerExistsOrDeleted validateIfUpdateCustomerExistsOrDeleted;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BookingEntryDTO bookingEntryDTO;
    @Mock
    private Customer customer;

    @Test
    @DisplayName("Verify if customer not exists by ID and throws exception")
    void verifyValidatorsBookingsScenario01() {

        //Arrange
        BDDMockito.given(customerRepository.existsById(bookingEntryDTO.customerId())).willReturn(false);

        //Act & Asserts
        Assertions.assertThrows(CustomerNotFoundException.class, () -> validateIfUpdateCustomerExistsOrDeleted
                .verifyValidatorsBookings(bookingEntryDTO, null, null));
    }

    @Test
    @DisplayName("Verify if customer is deleted and throws exception")
    void verifyValidatorsBookingsScenario02() {

        //Arrange
        BDDMockito.given(customerRepository.existsById(bookingEntryDTO.customerId())).willReturn(true);
        BDDMockito.given(customer.getIsDeleted()).willReturn(true);
        //Act & Asserts
        Assertions.assertThrows(CustomerNotFoundException.class, () -> validateIfUpdateCustomerExistsOrDeleted
                .verifyValidatorsBookings(bookingEntryDTO, null, customer));
    }

    @Test
    @DisplayName("Verify if customer exists and not been deleted and not throws exception")
    void verifyValidatorsBookingsScenario03() {

        //Arrange
        BDDMockito.given(customerRepository.existsById(bookingEntryDTO.customerId())).willReturn(true);
        BDDMockito.given(customer.getIsDeleted()).willReturn(false);
        //Act & Asserts
        Assertions.assertDoesNotThrow(() -> validateIfUpdateCustomerExistsOrDeleted
                .verifyValidatorsBookings(bookingEntryDTO, null, customer));
    }

}
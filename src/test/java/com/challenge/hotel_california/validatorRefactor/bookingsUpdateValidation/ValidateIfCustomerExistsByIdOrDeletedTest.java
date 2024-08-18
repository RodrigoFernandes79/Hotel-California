package com.challenge.hotel_california.validatorRefactor.bookingsUpdateValidation;

import com.challenge.hotel_california.DTOs.BookingUpdateEntryDTO;
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
class ValidateIfCustomerExistsByIdOrDeletedTest {
    @InjectMocks
    private ValidateIfCustomerExistsByIdOrDeleted validateIfCustomerExistsByIdOrDeleted;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BookingUpdateEntryDTO bookingUpdateEntryDTO;
    @Mock
    private Customer customerFound;

    @Test
    @DisplayName("Verify if customer name not exists in database and throws exception")
    void verifyBookingsUpdateValidatorsScenario01() {
        //Arrange
        BDDMockito.given(customerRepository.existsById(bookingUpdateEntryDTO.customerId())).willReturn(false);

//Act & Assert
        Assertions.assertThrows(CustomerNotFoundException.class, () -> validateIfCustomerExistsByIdOrDeleted
                .verifyBookingsUpdateValidators(bookingUpdateEntryDTO, null, null, customerFound, null));

    }

    @Test
    @DisplayName("Verify if customer name is already deleted in database and throws exception")
    void verifyBookingsUpdateValidatorsScenario02() {
        //Arrange
        BDDMockito.given(customerRepository.existsById(bookingUpdateEntryDTO.customerId())).willReturn(true);
        BDDMockito.given(customerFound.getIsDeleted()).willReturn(true);
//Act & Assert
        Assertions.assertThrows(CustomerNotFoundException.class, () -> validateIfCustomerExistsByIdOrDeleted
                .verifyBookingsUpdateValidators(bookingUpdateEntryDTO, null, null, customerFound, null));

    }

    @Test
    @DisplayName("Verify if customer name exists and not been deleted in database and not throws exception")
    void verifyBookingsUpdateValidatorsScenario03() {
        //Arrange
        BDDMockito.given(customerRepository.existsById(bookingUpdateEntryDTO.customerId())).willReturn(true);
        BDDMockito.given(customerFound.getIsDeleted()).willReturn(false);
//Act & Assert
        Assertions.assertDoesNotThrow(() -> validateIfCustomerExistsByIdOrDeleted
                .verifyBookingsUpdateValidators(bookingUpdateEntryDTO, null, null, customerFound, null));

    }
}
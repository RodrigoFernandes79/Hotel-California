package com.challenge.hotel_california.validatorRefactor.bookingsDeleteValidation;

import com.challenge.hotel_california.exceptions.BookingsNotFoundException;
import com.challenge.hotel_california.repository.BookingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidateIfBookingExistsByIdTest {
    @InjectMocks
    private ValidateIfBookingExistsById validateIfBookingExistsById;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    @DisplayName("Verify if booking not exists and throws exception")
    void verifyBookingsDeleteValidatorsScenario01() {
        //Arrange
        Long id = 1L;
        BDDMockito.given(bookingRepository.existsById(id)).willReturn(false);

        //Act & Assert
        Assertions.assertThrows(BookingsNotFoundException.class, () -> validateIfBookingExistsById
                .verifyBookingsDeleteValidators(id, null));
    }

    @Test
    @DisplayName("Verify if booking exists and not throws exception")
    void verifyBookingsDeleteValidatorsScenario02() {
        //Arrange
        Long id = 1L;
        BDDMockito.given(bookingRepository.existsById(id)).willReturn(true);

        //Act & Assert
        Assertions.assertDoesNotThrow(() -> validateIfBookingExistsById
                .verifyBookingsDeleteValidators(id, null));
    }
}
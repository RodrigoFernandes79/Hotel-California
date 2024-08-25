package com.challenge.hotel_california.validatorRefactor.bookingsUpdateCheckoutValidation;

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
class ValidateIfBookingsExistsByIdTest {
    @InjectMocks
    private ValidateIfBookingsExistsById validateIfBookingsExistsById;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    @DisplayName("Verify if reservation not exists by id and throws exception")
    void verifyBookingsCheckoutValidatorsScenario01() {
        //Arrange
        Long id = 1L;
        BDDMockito.given(bookingRepository.existsById(id)).willReturn(false);

        //Act & Assert
        Assertions.assertThrows(BookingsNotFoundException.class, () -> validateIfBookingsExistsById
                .verifyBookingsCheckoutValidators(id, null));
    }

    @Test
    @DisplayName("Verify if reservation exists by id and not throws exception")
    void verifyBookingsCheckoutValidatorsScenario02() {
        //Arrange
        Long id = 1L;
        BDDMockito.given(bookingRepository.existsById(id)).willReturn(true);

        //Act & Assert
        Assertions.assertDoesNotThrow(() -> validateIfBookingsExistsById
                .verifyBookingsCheckoutValidators(id, null));
    }
}
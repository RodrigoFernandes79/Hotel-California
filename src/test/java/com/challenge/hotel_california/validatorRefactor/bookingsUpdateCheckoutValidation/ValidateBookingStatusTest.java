package com.challenge.hotel_california.validatorRefactor.bookingsUpdateCheckoutValidation;

import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.RoomNotAvailableException;
import com.challenge.hotel_california.model.Booking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidateBookingStatusTest {
    @InjectMocks
    private ValidateBookingStatus validateBookingStatus;
    @Mock
    private Booking bookingFound;

    @Test
    @DisplayName("verify if a reservation is cancelled or completed and throws exception")
    void verifyBookingsCheckoutValidatorsScenario01() {
        //Arrange
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.CANCELLED);
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.COMPLETED);
        //Act & Assert
        Assertions.assertThrows(RoomNotAvailableException.class, () -> validateBookingStatus
                .verifyBookingsCheckoutValidators(null, bookingFound));
    }

    @Test
    @DisplayName("verify if a reservation is not cancelled or completed and not throws exception")
    void verifyBookingsCheckoutValidatorsScenario02() {
        //Arrange
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.CONFIRMED).willReturn(BookingStatus.CHECKED_IN).willReturn(BookingStatus.PENDING);
        //Act & Assert
        Assertions.assertDoesNotThrow(() -> validateBookingStatus
                .verifyBookingsCheckoutValidators(null, bookingFound));
    }
}
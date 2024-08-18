package com.challenge.hotel_california.validatorRefactor.bookingsUpdateValidation;

import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.BookingStatusException;
import com.challenge.hotel_california.model.Booking;
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
class ValidateIBookingStatusTest {
    @InjectMocks
    private ValidateIBookingStatus validateIBookingStatus;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private Booking bookingFound;

    @Test
    @DisplayName("Verify if booking status is cancelled or completed and throws exception")
    void verifyBookingsUpdateValidatorsScenario01() {
        //Arrange
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.COMPLETED).willReturn(BookingStatus.CANCELLED);

        //Act & Assert
        Assertions.assertThrows(BookingStatusException.class, () -> validateIBookingStatus
                .verifyBookingsUpdateValidators(null, null, bookingFound, null, null));
    }

    @Test
    @DisplayName("Verify if booking status is not cancelled or completed and not throws exception")
    void verifyBookingsUpdateValidatorsScenario02() {
        //Arrange
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.PENDING)
                .willReturn(BookingStatus.CONFIRMED).willReturn(BookingStatus.CHECKED_IN);

        //Act & Assert
        Assertions.assertDoesNotThrow(() -> validateIBookingStatus
                .verifyBookingsUpdateValidators(null, null, bookingFound, null, null));
    }
}
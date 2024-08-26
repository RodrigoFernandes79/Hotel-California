package com.challenge.hotel_california.validatorRefactor.bookingsDeleteValidation;

import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.BookingStatusException;
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
class ValidatorBookingsStatusTest {
    @InjectMocks
    private ValidatorBookingsStatus validatorBookingsStatus;
    @Mock
    private Booking bookingFound;

    @Test
    @DisplayName("Verify if booking status is CHECKED_IN, COMPLETED or CANCELLED and throws exception")
    void verifyBookingsDeleteValidatorsScenario01() {
        //Arrange
        var bookingStatusFound = bookingFound.getStatus();
        BDDMockito.given(bookingFound.getStatus()).willReturn(bookingStatusFound);
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.CHECKED_IN)
                .willReturn(BookingStatus.COMPLETED)
                .willReturn(BookingStatus.CANCELLED);
        //Act & Arrange
        Assertions.assertThrows(BookingStatusException.class, () -> validatorBookingsStatus
                .verifyBookingsDeleteValidators(null, bookingFound));
    }

    @Test
    @DisplayName("Verify if booking status is not CHECKED_IN, COMPLETED or CANCELLED and not throws exception")
    void verifyBookingsDeleteValidatorsScenario02() {
        //Arrange
        var bookingStatusFound = bookingFound.getStatus();
        BDDMockito.given(bookingFound.getStatus()).willReturn(bookingStatusFound);
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.PENDING).willReturn(BookingStatus.CONFIRMED);

        //Act & Arrange
        Assertions.assertDoesNotThrow(() -> validatorBookingsStatus
                .verifyBookingsDeleteValidators(null, bookingFound));
    }
}
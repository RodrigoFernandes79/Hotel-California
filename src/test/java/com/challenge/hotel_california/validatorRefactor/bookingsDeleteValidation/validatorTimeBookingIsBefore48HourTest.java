package com.challenge.hotel_california.validatorRefactor.bookingsDeleteValidation;

import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.BookingStatusException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.service.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class validatorTimeBookingIsBefore48HourTest {
    @InjectMocks
    private validatorTimeBookingIsBefore48Hour validatorTimeBookingIsBefore48Hour;
    @Mock
    private Booking bookingFound;
    @Mock
    private BookingService bookingService;

    @Test
    @DisplayName("Verify if difference in hour is less than 48 hours and throws exception")
    void verifyBookingsDeleteValidatorsScenario01() {
        //Arrange
        var checkInCurrentDate = LocalDateTime.now().plusHours(48);
        var cancelDate = LocalDateTime.now();

        var differenceInHours = Duration.between(cancelDate, checkInCurrentDate).toHours();
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.CONFIRMED);
        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(checkInCurrentDate);

        //Act & Assert
        Assertions.assertThrows(BookingStatusException.class, () -> validatorTimeBookingIsBefore48Hour
                .verifyBookingsDeleteValidators(null, bookingFound));
        Assertions.assertEquals(48, differenceInHours);

    }

    @Test
    @DisplayName("Verify if difference in hour is more than 48 hours and not throws exception")
    void verifyBookingsDeleteValidatorsScenario02() {
        //Arrange
        var checkInCurrentDate = LocalDateTime.now().plusHours(49);
        var cancelDate = LocalDateTime.now();
        var differenceInHours = Duration.between(cancelDate, checkInCurrentDate).toHours();
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.CONFIRMED);
        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(checkInCurrentDate);

        //Act & Assert
        Assertions.assertEquals(49, differenceInHours);
        Assertions.assertDoesNotThrow(() -> validatorTimeBookingIsBefore48Hour
                .verifyBookingsDeleteValidators(null, bookingFound));


    }
}
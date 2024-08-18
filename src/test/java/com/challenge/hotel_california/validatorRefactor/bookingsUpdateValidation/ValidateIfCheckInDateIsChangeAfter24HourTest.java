package com.challenge.hotel_california.validatorRefactor.bookingsUpdateValidation;

import com.challenge.hotel_california.DTOs.BookingUpdateEntryDTO;
import com.challenge.hotel_california.exceptions.BookingCheckInDateNotBeforeException;
import com.challenge.hotel_california.model.Booking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class ValidateIfCheckInDateIsChangeAfter24HourTest {
    @InjectMocks
    private ValidateIfCheckInDateIsChangeAfter24Hour validateIfCheckInDateIsChangeAfter24Hour;
    @Mock
    private Booking bookingFound;
    @Mock
    private BookingUpdateEntryDTO bookingUpdateEntryDTO;

    @Test
    @DisplayName("Verify if Check-in date not be changed to at least 24 hours later and throws exception")
    void verifyBookingsUpdateValidatorsScenario01() {
        //Arrange
        LocalDateTime currentCheckInDate = LocalDateTime.of(2024, 8, 18, 14, 00, 00);
        LocalDateTime newCheckInDate = LocalDateTime.of(2024, 8, 19, 11, 00, 00);

        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(currentCheckInDate);
        BDDMockito.given(bookingUpdateEntryDTO.checkInDate()).willReturn(newCheckInDate);

        //Assert
        Assertions.assertFalse(newCheckInDate.isAfter(currentCheckInDate.plusHours(24)));
        Assertions.assertFalse(currentCheckInDate.equals(newCheckInDate));
        //Assert & Act
        Assertions.assertThrows(BookingCheckInDateNotBeforeException.class, () -> validateIfCheckInDateIsChangeAfter24Hour
                .verifyBookingsUpdateValidators(bookingUpdateEntryDTO, null, bookingFound, null, null));
    }

    @Test
    @DisplayName("Verify if Check-in date can be changed if date are equals")
    void verifyBookingsUpdateValidatorsScenario02() {
        //Arrange
        LocalDateTime currentCheckInDate = LocalDateTime.of(2024, 8, 18, 14, 00, 00);
        LocalDateTime newCheckInDate = LocalDateTime.of(2024, 8, 18, 14, 00, 00);

        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(currentCheckInDate);
        BDDMockito.given(bookingUpdateEntryDTO.checkInDate()).willReturn(newCheckInDate);

        //Assert
        Assertions.assertTrue(currentCheckInDate.equals(newCheckInDate));
        //Assert & Act
        Assertions.assertDoesNotThrow(() -> validateIfCheckInDateIsChangeAfter24Hour
                .verifyBookingsUpdateValidators(bookingUpdateEntryDTO, null, bookingFound, null, null));
    }

    @Test
    @DisplayName("Verify if Check-in date can be changed if new check-in date is at least 24h")
    void verifyBookingsUpdateValidatorsScenario03() {
        //Arrange
        LocalDateTime currentCheckInDate = LocalDateTime.of(2024, 8, 18, 14, 00, 00);
        LocalDateTime newCheckInDate = LocalDateTime.of(2024, 8, 19, 14, 01, 00);

        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(currentCheckInDate);
        BDDMockito.given(bookingUpdateEntryDTO.checkInDate()).willReturn(newCheckInDate);

        //Assert
        Assertions.assertTrue(newCheckInDate.isAfter(currentCheckInDate.plusHours(24)));
        //Assert & Act
        Assertions.assertDoesNotThrow(() -> validateIfCheckInDateIsChangeAfter24Hour
                .verifyBookingsUpdateValidators(bookingUpdateEntryDTO, null, bookingFound, null, null));
    }
}
package com.challenge.hotel_california.validatorRefactor.bookingsUpdateValidation;

import com.challenge.hotel_california.DTOs.BookingUpdateEntryDTO;
import com.challenge.hotel_california.exceptions.BookingsNotFoundException;
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
class ValidateIfBookingIdAreTheSameTest {
    @InjectMocks
    private ValidateIfBookingIdAreTheSame validateIfBookingIdAreTheSame;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingUpdateEntryDTO bookingUpdateEntryDTO;
    @Mock
    private Booking bookingFound;

    @Test
    @DisplayName("Verify if booking ids are not the same and throws exception")
    void verifyBookingsUpdateValidatorsScenario01() {
        //Arrange
        BDDMockito.given(bookingFound.getId()).willReturn(2L);
        BDDMockito.given(bookingUpdateEntryDTO.id()).willReturn(1L);
        //Act & Assert
        Assertions.assertThrows(BookingsNotFoundException.class, () -> validateIfBookingIdAreTheSame
                .verifyBookingsUpdateValidators(bookingUpdateEntryDTO, null, bookingFound, null, null));

    }

    @Test
    @DisplayName("Verify if booking ids are the same and not throws exception")
    void verifyBookingsUpdateValidatorsScenario02() {
        //Arrange
        BDDMockito.given(bookingFound.getId()).willReturn(1L);
        BDDMockito.given(bookingUpdateEntryDTO.id()).willReturn(1L);
        //Act & Assert
        Assertions.assertDoesNotThrow(() -> validateIfBookingIdAreTheSame
                .verifyBookingsUpdateValidators(bookingUpdateEntryDTO, null, bookingFound, null, null));

    }
}
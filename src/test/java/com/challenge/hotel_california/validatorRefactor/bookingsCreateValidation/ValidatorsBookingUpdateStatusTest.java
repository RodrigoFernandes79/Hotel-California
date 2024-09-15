package com.challenge.hotel_california.validatorRefactor.bookingsCreateValidation;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.BookingsExistsException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.repository.BookingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ValidatorsBookingUpdateStatusTest {
    @InjectMocks
    private ValidatorsBookingUpdateStatus validatorsBookingUpdateStatus;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingEntryDTO bookingEntryDTO;
    @Mock
    private List<Booking> bookingsRoom;
 
    private List<BookingStatus> optionsStatusBooking;

    @Test
    @DisplayName("VCheck if room reservations are not active and throw an exception")
    void verifyValidatorsBookingsScenario01() {
        optionsStatusBooking = Arrays.asList(BookingStatus.CANCELLED, BookingStatus.COMPLETED);

        BDDMockito.given(bookingEntryDTO.roomId()).willReturn(1L);
        BDDMockito.given(bookingRepository.getBookingsById(bookingEntryDTO.roomId(), optionsStatusBooking)).willReturn(bookingsRoom);
        BDDMockito.given(bookingsRoom.isEmpty()).willReturn(false);

        //Act & Assert
        Assertions.assertThrows(BookingsExistsException.class, () -> validatorsBookingUpdateStatus
                .verifyValidatorsBookings(bookingEntryDTO, null, null));
    }

    @Test
    @DisplayName("VCheck if room reservations are active and not throw an exception")
    void verifyValidatorsBookingsScenario02() {
        optionsStatusBooking = Arrays.asList(BookingStatus.CANCELLED, BookingStatus.COMPLETED);

        BDDMockito.given(bookingEntryDTO.roomId()).willReturn(1L);
        BDDMockito.given(bookingRepository.getBookingsById(bookingEntryDTO.roomId(), optionsStatusBooking)).willReturn(bookingsRoom);
        BDDMockito.given(bookingsRoom.isEmpty()).willReturn(true);

        //Act & Assert
        Assertions.assertDoesNotThrow(() -> validatorsBookingUpdateStatus
                .verifyValidatorsBookings(bookingEntryDTO, null, null));
    }
}
package com.challenge.hotel_california.validatorRefactor.rooms;

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

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ValidateIfBookingsAreActiveTest {
    @InjectMocks
    private ValidateIfBookingsAreActive validateIfBookingsAreActive;
    @Mock
    private BookingRepository bookingRepository;
    @Spy
    private List<BookingStatus> optionsStatusBooking = new ArrayList<>();
    @Mock
    private List<Booking> bookingsRoom = new ArrayList<>();

    @Test
    @DisplayName("Verify if room reservation is active and throws Exception")
    void verifyRoomUpdateValidatorsScenario01() {
        //Arrange
        Long id = 1L;
        optionsStatusBooking.add(BookingStatus.CANCELLED);
        optionsStatusBooking.add(BookingStatus.COMPLETED);

        BDDMockito.given(bookingRepository.getBookingsById(id, optionsStatusBooking.stream().toList())).willReturn(bookingsRoom);
        BDDMockito.given(bookingsRoom.isEmpty()).willReturn(false);

        //Act & Asserts
        Assertions.assertThrows(BookingsExistsException.class, () -> validateIfBookingsAreActive.verifyRoomUpdateValidators(id, null));

    }

    @Test
    @DisplayName("Verify if room reservation is not active and pass")
    void verifyRoomUpdateValidatorsScenario02() {
        //Arrange
        Long id = 1L;
        optionsStatusBooking.add(BookingStatus.CANCELLED);
        optionsStatusBooking.add(BookingStatus.COMPLETED);

        BDDMockito.given(bookingRepository.getBookingsById(id, optionsStatusBooking.stream().toList())).willReturn(bookingsRoom);
        BDDMockito.given(bookingsRoom.isEmpty()).willReturn(true);

        //Act & Asserts
        Assertions.assertDoesNotThrow(() -> validateIfBookingsAreActive.verifyRoomUpdateValidators(id, null));

    }
}
package com.challenge.hotel_california.validatorRefactor.bookingsUpdateValidation;

import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.exceptions.BookingsExistsException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Room;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidateIfRoomAlreadyBookedTest {
    @InjectMocks
    private ValidateIfRoomAlreadyBooked validateIfRoomAlreadyBooked;
    @Mock
    private Room roomFound;
    @Mock
    private Booking bookingFound;
    private Room newRoom = new Room();

    @Test
    @DisplayName("Verify if room is already booked and throws exception")
    void verifyBookingsUpdateValidatorsScenario01() {
        //Arrange
        BDDMockito.given(roomFound.getStatus()).willReturn(RoomStatus.BOOKED);
        BDDMockito.given(roomFound.getId()).willReturn(1L);
        BDDMockito.given((bookingFound.getRoom())).willReturn(newRoom);

        //Act & Assert
        Assertions.assertFalse(roomFound.equals(bookingFound.getRoom()));
        Assertions.assertThrows(BookingsExistsException.class, () -> validateIfRoomAlreadyBooked
                .verifyBookingsUpdateValidators(null, roomFound, bookingFound, null, null));
    }

    @Test
    @DisplayName("Verify if room is already booked but the rooms are the same then not throws exception")
    void verifyBookingsUpdateValidatorsScenario02() {
        //Arrange
        BDDMockito.given(roomFound.getStatus()).willReturn(RoomStatus.BOOKED);
        BDDMockito.given(roomFound.getId()).willReturn(1L);
        BDDMockito.given((bookingFound.getRoom())).willReturn(roomFound);

        //Act & Assert
        Assertions.assertTrue(roomFound.equals(bookingFound.getRoom()));
        Assertions.assertDoesNotThrow(() -> validateIfRoomAlreadyBooked
                .verifyBookingsUpdateValidators(null, roomFound, bookingFound, null, null));
    }

    @Test
    @DisplayName("Verify if room is not already booked and rooms arent the same then not throws exception")
    void verifyBookingsUpdateValidatorsScenario03() {
        //Arrange
        BDDMockito.given(roomFound.getStatus()).willReturn(RoomStatus.AVAILABLE).willReturn(RoomStatus.INACTIVE).willReturn(RoomStatus.MAINTENANCE);
        BDDMockito.given((bookingFound.getRoom())).willReturn(newRoom);

        //Act & Assert
        Assertions.assertFalse(roomFound.equals(bookingFound.getRoom()));
        Assertions.assertDoesNotThrow(() -> validateIfRoomAlreadyBooked
                .verifyBookingsUpdateValidators(null, roomFound, bookingFound, null, null));
    }
}
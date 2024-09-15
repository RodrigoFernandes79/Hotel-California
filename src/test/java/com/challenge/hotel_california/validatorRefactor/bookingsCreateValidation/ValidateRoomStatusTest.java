package com.challenge.hotel_california.validatorRefactor.bookingsCreateValidation;

import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.exceptions.RoomNotAvailableException;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.RoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidateRoomStatusTest {
    @InjectMocks
    private ValidateRoomStatus validateRoomStatus;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private Room room;

    @Test
    @DisplayName("Verify if room status is available and NOT throws exception")
    void verifyValidatorsBookingsScenario01() {
        //Arrange
        BDDMockito.given(room.getStatus()).willReturn(RoomStatus.AVAILABLE);

        //Act & Asserts
        Assertions.assertDoesNotThrow(() -> validateRoomStatus
                .verifyValidatorsBookings(null, room, null));
    }

    @Test
    @DisplayName("Verify if room status is NOT available and throws exception")
    void verifyValidatorsBookingsScenario02() {
        //Arrange
        BDDMockito.given(room.getStatus()).willReturn(RoomStatus.BOOKED).willReturn(RoomStatus.INACTIVE).willReturn(RoomStatus.MAINTENANCE);

        //Act & Asserts
        Assertions.assertThrows(RoomNotAvailableException.class, () -> validateRoomStatus
                .verifyValidatorsBookings(null, room, null));
    }
}
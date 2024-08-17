package com.challenge.hotel_california.validatorRefactor.rooms;

import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
import com.challenge.hotel_california.exceptions.RoomNotFoundException;
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
class ValidateIfRoomsIdsAreEqualsTest {
    @InjectMocks
    private ValidateIfRoomsIdsAreEquals validateIfRoomsIdsAreEquals;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private Room room;
    @Mock
    private RoomEntryUpdateDTO roomEntryUpdateDTO;

    @Test
    @DisplayName("Verify if id rooms are not equals and throws exception")
    void verifyRoomUpdateValidatorsScenario01() {
        //Arrange
        Long id = 1L;

        BDDMockito.given(roomRepository.getReferenceById(id)).willReturn(room);
        BDDMockito.given(room.getId()).willReturn(3L);
        BDDMockito.given(roomEntryUpdateDTO.id()).willReturn(2L);

        //Act & Assert
        Assertions.assertThrows(RoomNotFoundException.class, () -> validateIfRoomsIdsAreEquals.verifyRoomUpdateValidators(id, roomEntryUpdateDTO));

    }

    @Test
    @DisplayName("Verify if id rooms are equals and not throws exception")
    void verifyRoomUpdateValidatorsScenario02() {
        //Arrange
        Long id = 1L;

        BDDMockito.given(roomRepository.getReferenceById(id)).willReturn(room);
        BDDMockito.given(room.getId()).willReturn(2L);
        BDDMockito.given(roomEntryUpdateDTO.id()).willReturn(2L);

        //Act & Assert
        Assertions.assertDoesNotThrow(() -> validateIfRoomsIdsAreEquals.verifyRoomUpdateValidators(id, roomEntryUpdateDTO));

    }
}
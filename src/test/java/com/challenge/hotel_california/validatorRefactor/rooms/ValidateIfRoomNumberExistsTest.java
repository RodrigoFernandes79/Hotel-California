package com.challenge.hotel_california.validatorRefactor.rooms;

import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
import com.challenge.hotel_california.exceptions.NumberRoomFoundException;
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

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class ValidateIfRoomNumberExistsTest {
    @InjectMocks
    private ValidateIfRoomNumberExists validateIfRoomNumberExists;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private Room room;
    @Mock
    private RoomEntryUpdateDTO roomEntryUpdateDTO;
    @Mock
    private Optional<Room> foundRoomNumber;


    @Test
    @DisplayName("Verify if room number exists and throws exception")
    void verifyRoomUpdateValidatorsScenario01() {
        //Arrange
        Long id = 1L;
        BDDMockito.given(roomRepository.getReferenceById(id)).willReturn(room);
        BDDMockito.given(roomEntryUpdateDTO.number()).willReturn("103");
        BDDMockito.given(roomRepository.findByNumber(roomEntryUpdateDTO.number())).willReturn(foundRoomNumber);
        BDDMockito.given(room.getNumber()).willReturn("102");
        BDDMockito.given(foundRoomNumber.isPresent()).willReturn(true);
        //Act & Asserts
        Assertions.assertThrows(NumberRoomFoundException.class, () -> validateIfRoomNumberExists.verifyRoomUpdateValidators(id, roomEntryUpdateDTO));
    }

    @Test
    @DisplayName("Verify if room number not exists and not throws exception")
    void verifyRoomUpdateValidatorsScenario02() {
        //Arrange
        Long id = 1L;
        BDDMockito.given(roomRepository.getReferenceById(id)).willReturn(room);
        BDDMockito.given(roomEntryUpdateDTO.number()).willReturn("102");
        BDDMockito.given(roomRepository.findByNumber(roomEntryUpdateDTO.number())).willReturn(Optional.empty());
        BDDMockito.given(room.getNumber()).willReturn("102");
        //Act & Asserts
        Assertions.assertDoesNotThrow(() -> validateIfRoomNumberExists.verifyRoomUpdateValidators(id, roomEntryUpdateDTO));
    }
}
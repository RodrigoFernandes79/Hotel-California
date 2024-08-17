package com.challenge.hotel_california.validatorRefactor.rooms;

import com.challenge.hotel_california.exceptions.RoomNotFoundException;
import com.challenge.hotel_california.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ValidateIfRoomExistByIdTest {
    @InjectMocks
    private ValidateIfRoomExistById validateIfRoomExistById;
    @Mock
    private RoomRepository roomRepository;


    @Test
    @DisplayName("Verify if room not exists by id and throws an exception")
    void verifyRoomUpdateValidatorsScenario01() {
        //Arrange
        Long id = 1L;
        given(roomRepository.existsById(id)).willReturn(false);


        //Act & Asserts
        assertThrows(RoomNotFoundException.class, () -> validateIfRoomExistById.verifyRoomUpdateValidators(id, null));
    }
    @Test
    @DisplayName("Verify if room exists by id and not throws an exception")
    void verifyRoomUpdateValidatorsScenario02() {
        //Arrange
        Long id = 1L;
        given(roomRepository.existsById(id)).willReturn(true);


        //Act & Asserts
        assertDoesNotThrow( () -> validateIfRoomExistById.verifyRoomUpdateValidators(id, null));
    }
}
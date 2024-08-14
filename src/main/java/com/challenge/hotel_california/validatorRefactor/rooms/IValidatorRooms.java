package com.challenge.hotel_california.validatorRefactor.rooms;

import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
import com.challenge.hotel_california.model.Room;

public interface IValidatorRooms {
    void verifyRoomUpdateValidators(Long id, RoomEntryUpdateDTO roomEntryUpdateDTO);

}

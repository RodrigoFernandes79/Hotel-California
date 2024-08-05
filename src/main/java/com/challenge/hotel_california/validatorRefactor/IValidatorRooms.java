package com.challenge.hotel_california.validatorRefactor;

import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
import com.challenge.hotel_california.model.Room;

public interface IValidatorRooms {
    void verifyRoomUpdateValidators(Long id, Room room, RoomEntryUpdateDTO roomEntryUpdateDTO);

}

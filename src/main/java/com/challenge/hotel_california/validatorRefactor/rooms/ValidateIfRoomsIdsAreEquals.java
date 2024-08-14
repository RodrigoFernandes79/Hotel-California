package com.challenge.hotel_california.validatorRefactor.rooms;

import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
import com.challenge.hotel_california.exceptions.RoomNotFoundException;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfRoomsIdsAreEquals implements IValidatorRooms {
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void verifyRoomUpdateValidators(Long id, RoomEntryUpdateDTO roomEntryUpdateDTO) {
        Room room = roomRepository.getReferenceById(id);

        if (!room.getId().equals(roomEntryUpdateDTO.id())) {
            throw new RoomNotFoundException("Room " + id + " not the same of ID: " + roomEntryUpdateDTO.id());
        }
    }

}

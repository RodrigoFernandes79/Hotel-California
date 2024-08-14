package com.challenge.hotel_california.validatorRefactor.rooms;

import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
import com.challenge.hotel_california.exceptions.NumberRoomFoundException;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfRoomNumberExists implements IValidatorRooms {
    @Autowired
    private RoomRepository roomRepository;
    @Override
    public void verifyRoomUpdateValidators(Long id, RoomEntryUpdateDTO roomEntryUpdateDTO) {
        Room room = roomRepository.getReferenceById(id);
        var foundRoomNumber = roomRepository.findByNumber(roomEntryUpdateDTO.number());
        if (!room.getNumber().equals(roomEntryUpdateDTO.number()) && foundRoomNumber.isPresent()) {
            throw new NumberRoomFoundException("Room " + roomEntryUpdateDTO.number() + " already Exists");
        }
    }
}

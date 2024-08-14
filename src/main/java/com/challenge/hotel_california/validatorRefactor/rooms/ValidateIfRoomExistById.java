package com.challenge.hotel_california.validatorRefactor.rooms;

import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
import com.challenge.hotel_california.exceptions.RoomNotFoundException;
import com.challenge.hotel_california.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfRoomExistById implements IValidatorRooms {
    @Autowired
    private RoomRepository roomRepository;


    @Override
    public void verifyRoomUpdateValidators(Long id, RoomEntryUpdateDTO roomEntryUpdateDTO) {
        if (!roomRepository.existsById(id)) {
            throw new RoomNotFoundException("Room " + id + " Not Found in Hotel California Database");
        }

    }

}

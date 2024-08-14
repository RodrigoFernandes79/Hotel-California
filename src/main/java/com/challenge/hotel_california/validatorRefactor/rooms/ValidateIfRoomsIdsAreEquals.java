package com.challenge.hotel_california.validatorRefactor.rooms;

import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.BookingsExistsException;
import com.challenge.hotel_california.exceptions.NumberRoomFoundException;
import com.challenge.hotel_california.exceptions.RoomNotFoundException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.BookingRepository;
import com.challenge.hotel_california.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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

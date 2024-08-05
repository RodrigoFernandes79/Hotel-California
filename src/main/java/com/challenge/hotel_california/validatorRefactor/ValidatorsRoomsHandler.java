package com.challenge.hotel_california.validatorRefactor;

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
import java.util.stream.Collectors;

@Component
public class ValidatorsRoomsHandler implements IValidatorRooms {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void verifyRoomUpdateValidators(Long id, Room room, RoomEntryUpdateDTO roomEntryUpdateDTO) {
        if (!roomRepository.existsById(id)) {
            throw new RoomNotFoundException("Room " + id + " Not Found in Hotel California Database");
        }
        if (!room.getId().equals(roomEntryUpdateDTO.id())) {
            throw new RoomNotFoundException("Room " + id + " not the same of ID: " + roomEntryUpdateDTO.id());
        }
        List<Room> roomList = roomRepository.findAll();
        var foundRoomNumber = roomList.stream().filter(r -> r.getNumber().equals(roomEntryUpdateDTO.number())).collect(Collectors.toList());
        if (!room.getNumber().equals(roomEntryUpdateDTO.number()) && !foundRoomNumber.isEmpty()) {
            throw new NumberRoomFoundException("Room " + roomEntryUpdateDTO.number() + " already Exists");
        }
        List<BookingStatus> optionsStatusBooking = Arrays.asList(BookingStatus.CANCELLED, BookingStatus.COMPLETED);
        List<Booking> bookingsRoom = bookingRepository.getBookingsById(id, optionsStatusBooking);
        if (!bookingsRoom.isEmpty()) {
            throw new BookingsExistsException("There are active bookings for this room.");
        }
    }

}

package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.RoomEntryDTO;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.exceptions.BookingsExistsException;
import com.challenge.hotel_california.exceptions.RoomNotFoundException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.BookingRepository;
import com.challenge.hotel_california.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;

    public Room addRoom(RoomEntryDTO roomEntryDTO) {
        Room room = new Room(roomEntryDTO);
        return roomRepository.save(room);
    }

    public List<Room> listAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms;
    }

    public Room getDetailsOfASpecificRoom(Long id) {
        Room room = roomRepository.getReferenceById(id);
        if (!roomRepository.existsById(id)) {
            throw new RoomNotFoundException("Room " + id + " Not Found in Hotel California Database");
        }
        return room;
    }

    public Room updateAnExistingRoom(RoomEntryDTO roomEntryDTO, Long id) {
        Room room = roomRepository.getReferenceById(id);
        if (!roomRepository.existsById(id)) {
            throw new RoomNotFoundException("Room " + id + " Not Found in Hotel California Database");
        }
        room.updateRoom(roomEntryDTO);
        return roomRepository.save(room);
    }

    public Room inactivateARoom(Long id) {
        Room room = roomRepository.getReferenceById(id);

        if (!roomRepository.existsById(id)) {
            throw new RoomNotFoundException("Room " + id + " Not Found in Hotel California Database");
        }
        List<BookingStatus> optionsStatusBooking = Arrays.asList(BookingStatus.CANCELLED, BookingStatus.COMPLETED);
        List<Booking> bookingsRoom = bookingRepository.getBookingsById(id, optionsStatusBooking);
        if (!bookingsRoom.isEmpty()) {
            throw new BookingsExistsException("There are active bookings for this room.");
        }
        room.setStatus(RoomStatus.INACTIVE);
        roomRepository.save(room);
        return room;
    }
}

package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.RoomEntryDTO;
import com.challenge.hotel_california.DTOs.RoomOutputGetListDTO;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.exceptions.BookingsExistsException;
import com.challenge.hotel_california.exceptions.NumberRoomFoundException;
import com.challenge.hotel_california.exceptions.RoomListNotFoundException;
import com.challenge.hotel_california.exceptions.RoomNotFoundException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.BookingRepository;
import com.challenge.hotel_california.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        var foundRoomNumber = roomRepository.findByNumber(roomEntryDTO.number());
        if(foundRoomNumber.isPresent()) {
            throw new NumberRoomFoundException("Room Number already exist");
        }
        Room room = new Room(roomEntryDTO);
        return roomRepository.save(room);
    }

    public Page<RoomOutputGetListDTO> listAllRooms(Pageable pageable) {
        Page<Room> rooms = roomRepository.findAll(pageable);
        if (rooms.isEmpty()) {
            throw new RoomListNotFoundException("No list rooms found");
        }
        Page<RoomOutputGetListDTO> getListDTOPage = rooms.map(RoomOutputGetListDTO::new);
        return getListDTOPage;
    }

    public Room getDetailsOfASpecificRoom(Long id) {
        Room room = roomRepository.getReferenceById(id);
        if (!roomRepository.existsById(id)) {
            throw new RoomNotFoundException("Room " + id + " Not Found in Hotel California Database");
        }
        return room;
    }

    public Room updateAnExistingRoom(RoomEntryDTO roomEntryDTO, Long id) {
        var foundRoomNumber = roomRepository.findByNumber(roomEntryDTO.number());
        if(foundRoomNumber.isPresent()) {
            throw new NumberRoomFoundException("Room Number already exist");
        }
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

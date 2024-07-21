package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.RoomEntryDTO;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public Room addRoom(RoomEntryDTO roomEntryDTO) {
        Room room = new Room(roomEntryDTO);
        return roomRepository.save(room);
    }

    public List<Room> listAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms;
    }
}

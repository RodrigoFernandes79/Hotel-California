package com.challenge.hotel_california.controller;

import com.challenge.hotel_california.DTOs.RoomEntryDTO;
import com.challenge.hotel_california.DTOs.RoomGetByIdDTO;
import com.challenge.hotel_california.DTOs.RoomOutputGetListDTO;
import com.challenge.hotel_california.DTOs.RoomOutputPostDTO;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.service.RoomService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping
    @Transactional
    public ResponseEntity<RoomOutputPostDTO> addRoom(@RequestBody RoomEntryDTO roomEntryDTO,
                                                     UriComponentsBuilder uriComponentsBuilder) {

        Room room = roomService.addRoom(roomEntryDTO);

        var uri = uriComponentsBuilder.path("/rooms/{id}")
                .buildAndExpand(room.getId()).toUri();
        return ResponseEntity.created(uri).body(new RoomOutputPostDTO(room));

    }

    @GetMapping
    public ResponseEntity<List<RoomOutputGetListDTO>> listAllRooms() {
        List<Room> rooms = roomService.listAllRooms();

        List<RoomOutputGetListDTO> roomOutputGetListDTOS = rooms.stream()
                .map(RoomOutputGetListDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(roomOutputGetListDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomGetByIdDTO> getDetailsOfASpecificRoom(@PathVariable Long id) {
        Room room = roomService.getDetailsOfASpecificRoom(id);

        return ResponseEntity.ok().body(new RoomGetByIdDTO(room));
    }
}

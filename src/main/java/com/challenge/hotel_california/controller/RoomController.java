package com.challenge.hotel_california.controller;

import com.challenge.hotel_california.DTOs.RoomEntryDTO;
import com.challenge.hotel_california.DTOs.RoomOutputPostDTO;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.service.RoomService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
}

package com.challenge.hotel_california.controller;

import com.challenge.hotel_california.DTOs.*;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.service.RoomService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping
    @Transactional
    public ResponseEntity<RoomOutputPostDTO> addRoom(@Valid @RequestBody RoomEntryDTO roomEntryDTO,
                                                     UriComponentsBuilder uriComponentsBuilder) {

        Room room = roomService.addRoom(roomEntryDTO);

        var uri = uriComponentsBuilder.path("/rooms/{id}")
                .buildAndExpand(room.getId()).toUri();
        return ResponseEntity.created(uri).body(new RoomOutputPostDTO(room));

    }

    @GetMapping
    public ResponseEntity<Page<RoomOutputGetListDTO>> listAllRooms(@PageableDefault(size = 5, sort = {"number"}) Pageable pageable) {
        Page<RoomOutputGetListDTO> roomOutputGetListDTOS = roomService.listAllRooms(pageable);

        return ResponseEntity.ok().body(roomOutputGetListDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomGetByIdDTO> getDetailsOfASpecificRoom(@PathVariable Long id) {
        Room room = roomService.getDetailsOfASpecificRoom(id);

        return ResponseEntity.ok().body(new RoomGetByIdDTO(room));
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<RoomGetUpdateByIdDTO> updateAnExistingRoom( @Valid @RequestBody RoomEntryDTO roomEntryDTO,
                                                                     @PathVariable Long id) {
        Room room = roomService.updateAnExistingRoom(roomEntryDTO, id);
        return ResponseEntity.ok().body(new RoomGetUpdateByIdDTO(room));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public Map<String, String> inactivateARoom(@PathVariable Long id) {
        Room room = roomService.inactivateARoom(id);
        Map<String, String> message = new HashMap<>();
        message.put("Room inactivated successfully: ", String.valueOf(room.getStatus()));
        return message;
    }
}

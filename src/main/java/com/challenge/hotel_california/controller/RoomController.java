package com.challenge.hotel_california.controller;

import com.challenge.hotel_california.DTOs.*;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Room", description = "Endpoints for Managing Room")
@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Operation(
            summary = "Add a new Room",
            description = "Create a new room with the provided details",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = RoomOutputPostDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500",
                            content = @Content)
            }
    )
    @PostMapping
    @Transactional
    public ResponseEntity<RoomOutputPostDTO> addRoom(@Valid @RequestBody RoomEntryDTO roomEntryDTO,
                                                     UriComponentsBuilder uriComponentsBuilder) {

        Room room = roomService.addRoom(roomEntryDTO);

        var uri = uriComponentsBuilder.path("/rooms/{id}")
                .buildAndExpand(room.getId()).toUri();
        return ResponseEntity.created(uri).body(new RoomOutputPostDTO(room));

    }

    @Operation(
            summary = "Get Room List paginated",
            description = "Get a list of all the Rooms created with pagination feature",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoomOutputGetListDTO.class)))),
                    @ApiResponse(description = "No Content", responseCode = "204",
                            content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500",
                            content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<Page<RoomOutputGetListDTO>> listAllRooms(@PageableDefault(size = 5, sort = {"number"}) Pageable pageable) {
        Page<RoomOutputGetListDTO> roomOutputGetListDTOS = roomService.listAllRooms(pageable);

        return ResponseEntity.ok().body(roomOutputGetListDTOS);
    }

    @Operation(
            summary = "Get details of a specific Room",
            description = "Retrieve details of a specific room by ID",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = RoomGetByIdDTO.class))),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500",
                            content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RoomGetByIdDTO> getDetailsOfASpecificRoom(@PathVariable Long id) {
        Room room = roomService.getDetailsOfASpecificRoom(id);

        return ResponseEntity.ok().body(new RoomGetByIdDTO(room));
    }

    @Operation(
            summary = "Update an existing Room",
            description = "Update the details of an existing room",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = RoomGetUpdateByIdDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500",
                            content = @Content)
            }
    )
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<RoomGetUpdateByIdDTO> updateAnExistingRoom(@Valid @RequestBody RoomEntryUpdateDTO roomEntryUpdateDTO,
                                                                     @PathVariable Long id) {
        Room room = roomService.updateAnExistingRoom(roomEntryUpdateDTO, id);
        return ResponseEntity.ok().body(new RoomGetUpdateByIdDTO(room));
    }

    @Operation(
            summary = "Inactivate a Room",
            description = "Deactivate a room by ID",
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204",
                            content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500",
                            content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    @Transactional
    public Map<String, String> inactivateARoom(@PathVariable Long id) {
        Room room = roomService.inactivateARoom(id);
        Map<String, String> message = new HashMap<>();
        message.put("Room inactivated successfully: ", String.valueOf(room.getStatus()));
        return message;
    }
}

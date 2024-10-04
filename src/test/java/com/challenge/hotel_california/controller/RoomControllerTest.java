package com.challenge.hotel_california.controller;

import com.challenge.hotel_california.DTOs.*;
import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.enums.RoomType;
import com.challenge.hotel_california.exceptions.RoomListNotFoundException;
import com.challenge.hotel_california.exceptions.RoomNotFoundException;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.service.RoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class RoomControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private RoomService roomService;
    @Autowired
    private JacksonTester<RoomEntryDTO> roomEntryDTO;
    @Autowired
    private JacksonTester<RoomOutputPostDTO> roomOutputPostDTO;
    @Autowired
    private JacksonTester<Page<RoomOutputGetListDTO>> pageRoomOutPutGetListDTO;
    @Autowired
    private JacksonTester<RoomGetByIdDTO> roomGetByIdDTO;
    @Autowired
    private JacksonTester<RoomEntryUpdateDTO> roomEntryUpdateDTO;
    @Autowired
    private JacksonTester<RoomGetUpdateByIdDTO> roomGetUpdateByIdDTO;

    @Test
    @DisplayName("Should return status 400 when method addRoom if get errors")
    void addRoomScenario01() throws Exception {
        //ARRANGE
        String json = "{}";
        //ACT
        var response = mvc.perform(
                post("/rooms")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Should return status 201 when method addRoom if not get errors")
    void addRoomScenario02() throws Exception {
        //ARRANGE
        Room roomMock = new Room(1l, "71", BigDecimal.valueOf(250.00), RoomType.SUITE, RoomStatus.AVAILABLE, null);

        when(roomService.addRoom(any(RoomEntryDTO.class))).thenReturn(roomMock);
        //ACT
        var response = mvc.perform(
                        post("/rooms")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(roomEntryDTO
                                        .write(new RoomEntryDTO(
                                                roomMock.getNumber(), roomMock.getPrice(), roomMock.getType(), roomMock.getStatus())
                                        ).getJson())
                )
                .andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(201, response.getStatus());
        var jsonResponse = response.getContentAsString();
        var jsonExpected = roomOutputPostDTO.write(new RoomOutputPostDTO(roomMock)).getJson();
        Assertions.assertTrue(jsonExpected.equals(jsonResponse));
    }

    @Test
    @DisplayName("Should return status 404 when method listAllRooms is empty")
    void listAllRoomsScenario01() throws Exception {
        //ARRANGE
        when(roomService.listAllRooms(any(Pageable.class))).thenThrow(RoomListNotFoundException.class);
        //ACT
        var response = mvc.perform(
                get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Should return status 200 when method listAllRooms returns data")
    void listAllRoomsScenario02() throws Exception {
        //ARRANGE
        Pageable pageable = PageRequest.of(0, 5, Sort.by("number"));
        Room roomMock = new Room(1l, "71", BigDecimal.valueOf(250.00), RoomType.SUITE, RoomStatus.AVAILABLE, null);
        List<Room> roomList = new ArrayList<>();
        roomList.add(roomMock);
        Page<Room> roomPage = new PageImpl<>(roomList);
        when(roomService.listAllRooms(any(Pageable.class))).thenReturn(roomPage.map(RoomOutputGetListDTO::new));
        //ACT
        var response = mvc.perform(
                get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Should return status 200 when method getDetailsOfASpecificRoom is called")
    void getDetailsOfASpecificRoomScenario01() throws Exception {
        //ARRANGE
        Long id = 1L;
        Room roomMock = new Room(1l, "71", BigDecimal.valueOf(250.00), RoomType.SUITE, RoomStatus.AVAILABLE, null);
        when(roomService.getDetailsOfASpecificRoom(id)).thenReturn(roomMock);
        //ACT
        var response = mvc.perform(
                get("/rooms/{id}", id)
                        .content(roomGetByIdDTO.write(new RoomGetByIdDTO(roomMock))
                                .getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
        var jsonResponse = response.getContentAsString();
        var jsonExpected = roomGetByIdDTO.write(new RoomGetByIdDTO(roomMock))
                .getJson();
        Assertions.assertEquals(jsonExpected, jsonResponse);
    }

    @Test
    @DisplayName("Should return status 404 when method getDetailsOfASpecificRoom is empty")
    void getDetailsOfASpecificRoomScenario02() throws Exception {
        //ARRANGE
        Long id = 1L;
        when(roomService.getDetailsOfASpecificRoom(id)).thenThrow(RoomNotFoundException.class);
        //ACT
        var response = mvc.perform(
                get("/rooms/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Should return status 200 OK when method updateExistingRoom is called")
    void updateAnExistingRoomScenario01() throws Exception {
        //ARRANGE
        Long id = 1L;
        Room roomMock = new Room(1l, "71", BigDecimal.valueOf(250.00), RoomType.SUITE, RoomStatus.AVAILABLE, null);
        when(roomService.updateAnExistingRoom(any(RoomEntryUpdateDTO.class), any())).thenReturn(roomMock);
        //ACT
        var response = mvc.perform(
                patch("/rooms/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roomEntryUpdateDTO.write(new RoomEntryUpdateDTO(roomMock.getId(), roomMock.getNumber(),
                                        roomMock.getPrice(), roomMock.getType()))
                                .getJson())
        ).andReturn().getResponse();
        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
        var jsonResponse = response.getContentAsString();
        var jsonExpected = roomGetUpdateByIdDTO.write(new RoomGetUpdateByIdDTO(roomMock)).getJson();

        Assertions.assertEquals(jsonExpected, jsonResponse);
    }

    @Test
    @DisplayName("Should returns a successful message and status http 200 when inactivate a room")
    void inactivateARoomScenario01() throws Exception {
        //ARRANGE
        Long id = 1L;
        Room roomMock = new Room(1l, "71", BigDecimal.valueOf(250.00), RoomType.SUITE, RoomStatus.INACTIVE, null);
        String message = String.format("{\"Room inactivated successfully: \":\"%s\"}", roomMock.getStatus());

        when(roomService.inactivateARoom(id)).thenReturn(roomMock);
        //ACT
        var response = mvc.perform(
                delete("/rooms/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(message, response.getContentAsString().toString());
    }

    @Test
    @DisplayName("Should returns status http 404 when id room not found")
    void inactivateARoomScenario02() throws Exception {
        //ARRANGE
        Long id = 1L;
        when(roomService.inactivateARoom(id)).thenThrow(RoomNotFoundException.class);
        //ACT
        var response = mvc.perform(
                delete("/rooms/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }
}
package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.RoomEntryDTO;
import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
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
import com.challenge.hotel_california.validatorRefactor.rooms.IValidatorRooms;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    @InjectMocks
    private RoomService roomService;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Spy
    private List<IValidatorRooms> verifyValidators = new ArrayList<>();
    @Mock
    private IValidatorRooms validator1;
    @Mock
    private IValidatorRooms validator2;

    @Mock
    private Optional<Room> foundRoomNumber;

    @Mock
    private RoomEntryDTO roomEntryDTO;

    @Captor
    private ArgumentCaptor<Room> roomArgumentCaptor;

    @Mock
    private Pageable pageable;
    @Mock
    private Page<Room> rooms;
    @Mock
    private Page<RoomOutputGetListDTO> getListDTOPage;
    @Mock
    private Room room;
    @Mock
    private RoomEntryUpdateDTO roomEntryUpdateDTO;
    @Mock
    private List<Booking> bookingsRoom;

    @Test
    @DisplayName("Should save a room when validate pass")
    void addRoomScenario01() {
        //Arrange
        BDDMockito.given(roomRepository.findByNumber(roomEntryDTO.number())).willReturn(foundRoomNumber);
        BDDMockito.given(foundRoomNumber.isPresent()).willReturn(false);
        //Act
        roomService.addRoom(roomEntryDTO);
        //Assert
        BDDMockito.then(roomRepository).should().save(roomArgumentCaptor.capture());

        Assertions.assertEquals(roomEntryDTO.number(), roomArgumentCaptor.getValue().getNumber());
        Assertions.assertEquals(roomEntryDTO.status(), roomArgumentCaptor.getValue().getStatus());

    }

    @Test
    @DisplayName("Should throws an exception when validate not pass")
    void addRoomScenario02() {
        //Arrange
        BDDMockito.given(roomRepository.findByNumber(roomEntryDTO.number())).willReturn(foundRoomNumber);
        BDDMockito.given(foundRoomNumber.isPresent()).willReturn(true);

        //Assert & Act
        Assertions.assertThrows(NumberRoomFoundException.class, () -> roomService.addRoom(roomEntryDTO));

    }

    @Test
    @DisplayName("Should return a list page when validate pass")
    void listAllRoomsScenario01() {
        //Arrange
        Room room1 = new Room(roomEntryDTO);
        List<Room> roomList = new ArrayList<>();
        roomList.add(room1);
        this.rooms = new PageImpl<>(roomList, pageable, roomList.size());
        BDDMockito.given(roomRepository.findAll(pageable)).willReturn(rooms);
        //Act
        getListDTOPage = roomService.listAllRooms(pageable);

        //Assert
        Assertions.assertAll(
                () -> Assertions.assertNotNull(getListDTOPage),
                () -> Assertions.assertEquals(1, getListDTOPage.getSize()),
                () -> Assertions.assertEquals(rooms.getTotalElements(), getListDTOPage.getTotalElements()),
                () -> Assertions.assertEquals(rooms.getTotalPages(), getListDTOPage.getTotalPages()),
                () -> Assertions.assertEquals(room1.getNumber(), getListDTOPage.getContent().get(0).number()),
                () -> Assertions.assertEquals(room1.getStatus(), getListDTOPage.getContent().get(0).status()),
                () -> Assertions.assertEquals(room1.getType(), getListDTOPage.getContent().get(0).type()),
                () -> Assertions.assertEquals(room1.getPrice(), getListDTOPage.getContent().get(0).price())
        );
    }

    @Test
    @DisplayName("Should return a exception when validate not pass")
    void listAllRoomsScenario02() {
        //Arrange
        BDDMockito.given(roomRepository.findAll(pageable)).willReturn(rooms);
        BDDMockito.given(rooms.isEmpty()).willReturn(true);
        //Assert & Act
        Assertions.assertThrows(RoomListNotFoundException.class, () -> roomService.listAllRooms(pageable));
    }

    @Test
    @DisplayName("Should save a room when the method is called")
    void updateAnExistingRoomScenario01() {
        //Arrange
        Long id = 1L;
        BDDMockito.given(roomRepository.getReferenceById(id)).willReturn(room);
        BDDMockito.doNothing().when(room).updateRoom(roomEntryUpdateDTO);
        //Act
        roomService.updateAnExistingRoom(roomEntryUpdateDTO, id);


        //Assertions
        BDDMockito.then(roomRepository).should().save(roomArgumentCaptor.capture());
        Room savedRoom = roomArgumentCaptor.getValue();
        Assertions.assertEquals(roomEntryUpdateDTO.id(), savedRoom.getId());
        Assertions.assertEquals(roomEntryUpdateDTO.number(), savedRoom.getNumber());

    }

    @Test
    @DisplayName("Should check validations")
    void updateAnExistingRoomScenario02() {
        //Arrange
        Long id = 1L;
        BDDMockito.given(roomRepository.getReferenceById(id)).willReturn(room);
        BDDMockito.doNothing().when(room).updateRoom(roomEntryUpdateDTO);
        verifyValidators.add(validator1);
        verifyValidators.add(validator2);
        //Act
        roomService.updateAnExistingRoom(roomEntryUpdateDTO, id);

        //Assertions
        BDDMockito.then(validator1).should().verifyRoomUpdateValidators(id, roomEntryUpdateDTO);
        BDDMockito.then(validator2).should().verifyRoomUpdateValidators(id, roomEntryUpdateDTO);

    }

    @Test
    @DisplayName("Should throws exception when id not found into database")
    void inactivateARoomScenario01() {
        //Arrange
        Long id = 2L;
        BDDMockito.given(roomRepository.getReferenceById(id)).willReturn(room);
        BDDMockito.given(roomRepository.existsById(id)).willReturn(false);

        //Act & Assert
        Assertions.assertThrows(RoomNotFoundException.class, () -> roomService.inactivateARoom(id));

    }

    @Test
    @DisplayName("Should throws exception when booking is active")
    void inactivateARoomScenario02() {
        //Arrange
        Long id = 2L;
        BDDMockito.given(roomRepository.getReferenceById(id)).willReturn(room);
        BDDMockito.given(roomRepository.existsById(id)).willReturn(true);

        List<BookingStatus> optionsStatusBooking = Arrays.asList(BookingStatus.CANCELLED, BookingStatus.COMPLETED);
        BDDMockito.given(bookingRepository.getBookingsById(id, optionsStatusBooking)).willReturn(bookingsRoom);
        BDDMockito.given(bookingsRoom.isEmpty()).willReturn(false);

        //Act & Assert
        Assertions.assertThrows(BookingsExistsException.class, () -> roomService.inactivateARoom(id));

    }

    @Test
    @DisplayName("Should save a room status when all validations passed")
    void inactivateARoomScenario03() {
        //Arrange
        Long id = 2L;
        BDDMockito.given(roomRepository.getReferenceById(id)).willReturn(room);
        BDDMockito.given(roomRepository.existsById(id)).willReturn(true);

        List<BookingStatus> optionsStatusBooking = Arrays.asList(BookingStatus.CANCELLED, BookingStatus.COMPLETED);
        BDDMockito.given(bookingRepository.getBookingsById(id, optionsStatusBooking)).willReturn(bookingsRoom);
        BDDMockito.given(bookingsRoom.isEmpty()).willReturn(true);
        room.setStatus(RoomStatus.INACTIVE);
        //Act
        roomService.inactivateARoom(id);

        //Arrange
        BDDMockito.then(roomRepository).should().save(roomArgumentCaptor.capture());
        Room savedRoom = roomArgumentCaptor.getValue();
        Assertions.assertEquals(roomEntryUpdateDTO.id(), savedRoom.getId());
        Assertions.assertEquals(roomEntryUpdateDTO.number(), savedRoom.getNumber());

    }
}



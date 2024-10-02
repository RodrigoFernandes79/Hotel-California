package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.DTOs.BookingUpdateEntryDTO;
import com.challenge.hotel_california.DTOs.CustomerEntryDTO;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.exceptions.BookingsNotFoundException;
import com.challenge.hotel_california.exceptions.CustomerNotFoundException;
import com.challenge.hotel_california.exceptions.RoomNotFoundException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.BookingRepository;
import com.challenge.hotel_california.repository.CustomerRepository;
import com.challenge.hotel_california.repository.RoomRepository;
import com.challenge.hotel_california.validatorRefactor.bookingsCreateValidation.IValidatorBookings;
import com.challenge.hotel_california.validatorRefactor.bookingsDeleteValidation.IValidatorBookingsDelete;
import com.challenge.hotel_california.validatorRefactor.bookingsUpdateCheckoutValidation.IValidatorBookingsCheckout;
import com.challenge.hotel_california.validatorRefactor.bookingsUpdateValidation.IValidatorBookingsUpdate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Spy
    private List<IValidatorBookings> verifyValidators = new ArrayList<>();
    @Mock
    private IValidatorBookings validatorBookings1;
    @Mock
    private IValidatorBookings validatorBookings2;
    @Spy
    private List<IValidatorBookingsUpdate> verifyUpdateBookingsValidators = new ArrayList<>();
    @Mock
    private IValidatorBookingsUpdate validatorBookingsUpdate1;
    @Mock
    private IValidatorBookingsUpdate validatorBookingsUpdate2;
    @Spy
    private List<IValidatorBookingsDelete> verifyDeleteBookingsValidators = new ArrayList<>();
    @Mock
    private IValidatorBookingsDelete validatorBookingsDelete1;
    @Mock
    private IValidatorBookingsDelete validatorBookingsDelete2;
    @Spy
    private List<IValidatorBookingsCheckout> verifyCheckoutValidators = new ArrayList<>();
    @Mock
    private IValidatorBookingsCheckout validatorBookingsCheckout1;
    @Mock
    private IValidatorBookingsCheckout validatorBookingsCheckout2;
    private BookingEntryDTO bookingEntryDTO;
    @Mock
    private Room room;
    @Mock
    private Customer customer;
    @Captor
    private ArgumentCaptor<Booking> argumentCaptor;
    @Mock
    private Pageable pageable;
    @Mock
    private Page<Booking> bookingsFound;
    @Mock
    private List<Customer> customerFound;
    @Mock
    private Booking bookingFound;
    private BookingUpdateEntryDTO bookingUpdateEntryDTO;

    @Test
    @DisplayName("Should save a reservation")
    void createAReservationScenario01() {
        //Arrange
        this.bookingEntryDTO = new BookingEntryDTO(customer.getId(), LocalDateTime.now().plusMonths(1), room.getId());
        BDDMockito.given(roomRepository.existsById(bookingEntryDTO.roomId())).willReturn(true);
        BDDMockito.given(roomRepository.getReferenceById(bookingEntryDTO.roomId())).willReturn(room);
        BDDMockito.given(customerRepository.getReferenceById(bookingEntryDTO.customerId())).willReturn(customer);
        //Act
        bookingService.createAReservation(bookingEntryDTO);
        //Assert
        BDDMockito.then(room).should().setStatus(RoomStatus.BOOKED);
        BDDMockito.then(bookingRepository).should().save(argumentCaptor.capture());
        Booking bookingSaved = argumentCaptor.getValue();

        Assertions.assertEquals(bookingEntryDTO.roomId(), bookingSaved.getRoom().getId());
        Assertions.assertEquals(bookingEntryDTO.checkInDate().toLocalDate(), bookingSaved.getCheckInDate().toLocalDate());
        Assertions.assertEquals(bookingEntryDTO.customerId(), bookingSaved.getCustomerName().getId());

    }

    @Test
    @DisplayName("Should Verify validators list before create a reservation")
    void createAReservationScenario02() {
        //Arrange
        this.bookingEntryDTO = new BookingEntryDTO(customer.getId(), LocalDateTime.now().plusMonths(1), room.getId());
        BDDMockito.given(roomRepository.existsById(bookingEntryDTO.roomId())).willReturn(true);
        BDDMockito.given(roomRepository.getReferenceById(bookingEntryDTO.roomId())).willReturn(room);
        BDDMockito.given(customerRepository.getReferenceById(bookingEntryDTO.customerId())).willReturn(customer);
        this.verifyValidators.add(validatorBookings1);
        this.verifyValidators.add(validatorBookings2);
        //Act
        bookingService.createAReservation(bookingEntryDTO);
        //Assert
        BDDMockito.then(validatorBookings1).should()
                .verifyValidatorsBookings(bookingEntryDTO, room, customer);
        BDDMockito.then(validatorBookings2).should()
                .verifyValidatorsBookings(bookingEntryDTO, room, customer);
    }

    @Test
    @DisplayName("Should show an exception when  room id not exist")
    void createAReservationScenario03() {
        //Arrange
        this.bookingEntryDTO = new BookingEntryDTO(customer.getId(), LocalDateTime.now().plusMonths(1), room.getId());
        BDDMockito.given(roomRepository.existsById(bookingEntryDTO.roomId())).willReturn(false);
        //Assert & ACt
        Assertions.assertThrows(RoomNotFoundException.class, () -> bookingService
                .createAReservation(bookingEntryDTO));
    }

    @Test
    @DisplayName("Should return a page reservation when validation pass")
    void listAllReservationsScenario01() {
        //Arrange
        Booking booking = new Booking(customer, LocalDateTime.now().plusMonths(1), room, room.getPrice());
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        this.bookingsFound = new PageImpl<>(bookingList, pageable, bookingList.size());

        BDDMockito.given(bookingRepository.findAll(pageable)).willReturn(bookingsFound);

        //Act
        bookingService.listAllReservations(pageable);
        //Assert
        Assertions.assertNotNull(bookingsFound);
        Assertions.assertEquals(1, bookingsFound.getTotalPages());
        Assertions.assertEquals(customer, bookingsFound.getContent().get(0).getCustomerName());
        Assertions.assertEquals(booking.getCheckInDate(), bookingsFound.getContent().get(0).getCheckInDate());
        Assertions.assertEquals(room.getPrice(), bookingsFound.getContent().get(0).getTotalPrice());
    }

    @Test
    @DisplayName("Should throws an exception when there's no reservation")
    void listAllReservationsScenario02() {
        //Arrange
        BDDMockito.given(bookingRepository.findAll(pageable)).willReturn(bookingsFound);
        BDDMockito.given(bookingsFound.isEmpty()).willReturn(true);
        //Assert & Act
        Assertions.assertThrows(BookingsNotFoundException.class, () -> bookingService
                .listAllReservations(pageable));
    }

    @Test
    @DisplayName("Should return all the reservations by customer name")
    void listAllReservationsByCustomerScenario01() {
        //Arrange
        CustomerEntryDTO customerEntryDTO = new CustomerEntryDTO("a name"
                , "emai@email.com", "93943494594");
        Customer customerObj = new Customer(customerEntryDTO);
        BDDMockito.given(customerRepository.findByNameContainingIgnoreCase(customerObj.getName()))
                .willReturn(customerFound);
        BDDMockito.given(customerFound.isEmpty()).willReturn(false);

        BDDMockito.given(bookingRepository.findAllByCustomerNameById(pageable, customerObj.getName()))
                .willReturn(bookingsFound);
        //Act
        bookingService.listAllReservationsByCustomer(pageable, customerObj.getName());
        //Asserts
        Assertions.assertNotNull(bookingsFound);
        Assertions.assertEquals("a name", customerObj.getName());

    }

    @Test
    @DisplayName("Should throws an exception when customer not found")
    void listAllReservationsByCustomerScenario02() {
        //Arrange
        CustomerEntryDTO customerEntryDTO = new CustomerEntryDTO("a name",
                "emai@email.com", "93943494594");
        Customer customerObj = new Customer(customerEntryDTO);
        BDDMockito.given(customerRepository.findByNameContainingIgnoreCase(customerObj.getName()))
                .willReturn(customerFound);
        BDDMockito.given(customerFound.isEmpty()).willReturn(true);

        //Asserts & Act
        Assertions.assertThrows(CustomerNotFoundException.class, () -> bookingService
                .listAllReservationsByCustomer(pageable, customerObj.getName()));

    }

    @Test
    @DisplayName("Should throws an exception when reservation not found")
    void listAllReservationsByCustomerScenario03() {
        //Arrange
        CustomerEntryDTO customerEntryDTO = new CustomerEntryDTO("a name", "emai@email.com",
                "93943494594");
        Customer customerObj = new Customer(customerEntryDTO);
        BDDMockito.given(customerRepository.findByNameContainingIgnoreCase(customerObj.getName()))
                .willReturn(customerFound);
        BDDMockito.given(customerFound.isEmpty()).willReturn(false);
        BDDMockito.given(bookingRepository.findAllByCustomerNameById(pageable, customerObj.getName()))
                .willReturn(bookingsFound);
        BDDMockito.given(bookingsFound.isEmpty()).willReturn(true);

        //Asserts & Act
        Assertions.assertThrows(BookingsNotFoundException.class, () -> bookingService
                .listAllReservationsByCustomer(pageable, customerObj.getName()));

    }

    @Test
    @DisplayName("Should save a updated booking")
    void updateReservationScenario01() {
        //Arrange
        Long id = 1L;
        BigDecimal roomPrice = new BigDecimal(200.00);

        this.bookingUpdateEntryDTO = new BookingUpdateEntryDTO(id, customer.getId(), LocalDateTime.now().plusMonths(1),
                BookingStatus.COMPLETED, room.getId());

        BDDMockito.given(bookingFound.getId()).willReturn(id);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);
        BDDMockito.given(bookingRepository.existsById(id)).willReturn(true);
        BDDMockito.given(bookingRepository.getReferenceById(id)).willReturn(bookingFound);
        BDDMockito.given(bookingFound.getCustomerName()).willReturn(customer);
        BDDMockito.given(customerRepository.getReferenceById(bookingUpdateEntryDTO.customerId()))
                .willReturn(customer);
        BDDMockito.given(roomRepository.getReferenceById(bookingUpdateEntryDTO.roomId()))
                .willReturn(room);
        BDDMockito.given(room.getPrice()).willReturn(roomPrice);
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.COMPLETED);
        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(LocalDateTime.now().plusMonths(1));

        //Act
        bookingService.updateReservation(bookingUpdateEntryDTO, id);
        //Assert
        BDDMockito.then(bookingFound).should().updateBooking(customer, bookingUpdateEntryDTO, room);
        BDDMockito.then(bookingRepository).should().save(argumentCaptor.capture());
        Booking bookingSaved = argumentCaptor.getValue();
        Assertions.assertEquals(bookingSaved.getCustomerName().getName(), customer.getName());
        Assertions.assertEquals(bookingSaved.getRoom(), room);
        Assertions.assertEquals(bookingSaved.getCheckInDate().toLocalDate(), bookingUpdateEntryDTO.checkInDate().toLocalDate());
    }

    @Test
    @DisplayName("Should verify a validation list")
    void updateReservationScenario02() {
        //Arrange
        Long id = 1L;
        BigDecimal roomPrice = new BigDecimal(200.00);

        this.bookingUpdateEntryDTO = new BookingUpdateEntryDTO(id, customer.getId(), LocalDateTime.now().plusMonths(1),
                BookingStatus.COMPLETED, room.getId());

        BDDMockito.given(bookingFound.getId()).willReturn(id);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);
        BDDMockito.given(bookingRepository.existsById(id)).willReturn(true);
        BDDMockito.given(bookingRepository.getReferenceById(id)).willReturn(bookingFound);
        BDDMockito.given(bookingFound.getCustomerName()).willReturn(customer);
        BDDMockito.given(customerRepository.getReferenceById(bookingUpdateEntryDTO.customerId()))
                .willReturn(customer);
        BDDMockito.given(roomRepository.getReferenceById(bookingUpdateEntryDTO.roomId()))
                .willReturn(room);
        BDDMockito.given(room.getPrice()).willReturn(roomPrice);
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.COMPLETED);
        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(LocalDateTime.now().plusMonths(1));
        this.verifyUpdateBookingsValidators.add(validatorBookingsUpdate1);
        this.verifyUpdateBookingsValidators.add(validatorBookingsUpdate2);

        //Act
        bookingService.updateReservation(bookingUpdateEntryDTO, id);
        //Assert
        BDDMockito.then(validatorBookingsUpdate1).should()
                .verifyBookingsUpdateValidators(bookingUpdateEntryDTO, room, bookingFound, customer, id);
        BDDMockito.then(validatorBookingsUpdate2).should()
                .verifyBookingsUpdateValidators(bookingUpdateEntryDTO, room, bookingFound, customer, id);
    }

    @Test
    @DisplayName("Should change the room and booking status")
    void deleteAReservationScenario01() {
        //Arrange
        Long id = 1L;
        BigDecimal roomPrice = new BigDecimal(200.00);
        BDDMockito.given(bookingRepository.getReferenceById(id)).willReturn(bookingFound);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.COMPLETED);
        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(LocalDateTime.now().plusMonths(1L));
        BDDMockito.given(room.getPrice()).willReturn(roomPrice);
        BDDMockito.given(bookingFound.getCustomerName()).willReturn(customer);

        //Act
        bookingService.deleteAReservation(id);

        //Assert
        BDDMockito.then(bookingFound).should().setStatus(BookingStatus.CANCELLED);
        BDDMockito.then(room).should()
                .setStatus(RoomStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Should verify the validations when the method delete is called")
    void deleteAReservationScenario02() {
        //Arrange
        Long id = 1L;
        BigDecimal roomPrice = new BigDecimal(200.00);
        BDDMockito.given(bookingRepository.getReferenceById(id)).willReturn(bookingFound);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);
        BDDMockito.given(bookingFound.getStatus()).willReturn(BookingStatus.COMPLETED);
        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(LocalDateTime.now().plusMonths(1L));
        BDDMockito.given(room.getPrice()).willReturn(roomPrice);
        BDDMockito.given(bookingFound.getCustomerName()).willReturn(customer);
        this.verifyDeleteBookingsValidators.add(validatorBookingsDelete1);
        this.verifyDeleteBookingsValidators.add(validatorBookingsDelete2);
        //Act
        bookingService.deleteAReservation(id);

        //Assert
        BDDMockito.then(validatorBookingsDelete1).should().verifyBookingsDeleteValidators(id, bookingFound);
        BDDMockito.then(validatorBookingsDelete2).should().verifyBookingsDeleteValidators(id, bookingFound);
    }

    @Test
    @DisplayName("Should update a new checkout date when the method is called")
    void updateCheckOutDateScenario01() {
        //Arrange
        Long id = 1L;
        var checkOutDate = LocalDateTime.now();
        BDDMockito.given(bookingRepository.getReferenceById(id)).willReturn(bookingFound);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);
        BDDMockito.mockStatic(LocalDateTime.class); // Mock static method
        BDDMockito.given(LocalDateTime.now()).willReturn(checkOutDate);
        //Act
        bookingService.updateCheckOutDate(id);
        //Assert

        BDDMockito.then(bookingFound).should().setCheckOutDate(checkOutDate);
        BDDMockito.then(bookingFound.getRoom()).should().setStatus(RoomStatus.AVAILABLE);
        BDDMockito.then(bookingFound).should().setStatus(BookingStatus.COMPLETED);

    }

    @Test
    @DisplayName("Should verify validations when the method update checkout Date is called")
    void updateCheckOutDateScenario02() {
        //Arrange
        Long id = 1L;
        BDDMockito.given(bookingRepository.getReferenceById(id)).willReturn(bookingFound);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);

        verifyCheckoutValidators.add(validatorBookingsCheckout1);
        verifyCheckoutValidators.add(validatorBookingsCheckout2);
        //Act
        bookingService.updateCheckOutDate(id);
        //Assert
        BDDMockito.then(validatorBookingsCheckout1).should().verifyBookingsCheckoutValidators(id, bookingFound);
        BDDMockito.then(validatorBookingsCheckout2).should().verifyBookingsCheckoutValidators(id, bookingFound);


    }
}

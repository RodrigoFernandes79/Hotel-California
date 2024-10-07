package com.challenge.hotel_california.controller;

import com.challenge.hotel_california.DTOs.*;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.BookingsNotFoundException;
import com.challenge.hotel_california.exceptions.CustomerNotFoundException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.service.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class BookingControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookingService bookingService; //mock the bookingService class
    @Autowired
    private JacksonTester<BookingEntryDTO> bookingEntryDTOJacksonTester;
    @Autowired
    private JacksonTester<BookingOutputDTO> bookingOutputDTOJacksonTester;
    @Autowired
    private JacksonTester<BookingOutputListDTO> bookingOutputListDTOJacksonTester;
    @Autowired
    private JacksonTester<BookingUpdateEntryDTO> bookingUpdateEntryDTOJacksonTester;
    @Autowired
    private JacksonTester<BookingDeleteStatusDTO> bookingDeleteStatusDTOJacksonTester;

    @Test
    @DisplayName("Should return a 400 code to createAReservation with error")
    void createANewReservationScenario01() throws Exception {
        //ARRANGE
        String json = "{}";
        //ACT
        var response = mvc.perform(
                        post("/bookings")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()
                .getResponse();
        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Should return a 201 code to createAReservation without error")
    void createANewReservationScenario02() throws Exception {
        //ARRANGE
        BookingEntryDTO dto = new BookingEntryDTO(1L, LocalDateTime.of(2025, 11, 07, 22, 00, 00), 1L);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCustomerName(new Customer());
        booking.setRoom(new Room());
        booking.setCheckInDate(dto.checkInDate());

        BDDMockito.when(bookingService
                .createAReservation(dto)).thenReturn(booking);
        //ACT
        var response = mvc.perform(
                        post("/bookings")
                                .content(bookingEntryDTOJacksonTester.write(dto).getJson())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()
                .getResponse();
        //ASSERT
        Assertions.assertEquals(201, response.getStatus());
        var jsonExpected = bookingOutputDTOJacksonTester.write(new BookingOutputDTO(booking)).getJson();
        Assertions.assertEquals(jsonExpected, response.getContentAsString());
    }

    @Test
    @DisplayName("Should return a code status 200 when  returns a list")
    void listAllReservationsScenario01() throws Exception {
        //ARRANGE
        BookingEntryDTO dto = new BookingEntryDTO(1L, LocalDateTime.of(2025, 11, 07, 22, 00, 00), 1L);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCustomerName(new Customer());
        booking.setRoom(new Room());
        booking.setCheckInDate(dto.checkInDate());
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        Page<Booking> BookingDTOPage = new PageImpl<>(bookingList);
        BDDMockito.when(bookingService.listAllReservations(any(Pageable.class))).thenReturn(BookingDTOPage.map(BookingOutputListDTO::new));
        //ACT
        var response = mvc.perform(
                get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
        var jsonExpected = bookingOutputListDTOJacksonTester.write(new BookingOutputListDTO(booking)).getJson();
        Assertions.assertTrue(response.getContentAsString().contains(jsonExpected));
    }

    @Test
    @DisplayName("Should return a code status 404 when  returns a empty List")
    void listAllReservationsScenario02() throws Exception {
        //ARRANGE
        BDDMockito.when(bookingService.listAllReservations(any(Pageable.class))).thenThrow(BookingsNotFoundException.class);
        //ACT
        var response = mvc.perform(
                get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Should return a status code 200 when return a list by customer name")
    void listAllReservationsByCustomerScenario01() throws Exception {
        //ARRANGE
        String customerName = "a customer name";
        BookingEntryDTO dto = new BookingEntryDTO(1L, LocalDateTime.of(2025, 11, 07, 22, 00, 00), 1L);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCustomerName(new Customer());
        booking.setRoom(new Room());
        booking.setCheckInDate(dto.checkInDate());
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        Page<Booking> BookingDTOPage = new PageImpl<>(bookingList);

        BDDMockito.when(bookingService.listAllReservationsByCustomer(any(Pageable.class), eq(customerName))).thenReturn(BookingDTOPage.map(BookingOutputListDTO::new));
        //ACT
        var response = mvc.perform(
                get("/bookings/customer_name")
                        .param("customerName", customerName)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
        var jsonExpected = bookingOutputListDTOJacksonTester.write(new BookingOutputListDTO(booking)).getJson();
        Assertions.assertTrue(response.getContentAsString().contains(jsonExpected));
    }

    @Test
    @DisplayName("Should return a code status 404 when customer name not found")
    void listAllReservationsByCustomerScenario02() throws Exception {
        //ARRANGE
        String customerName = "a customer name";
        BDDMockito.when(bookingService.listAllReservationsByCustomer(any(Pageable.class), eq(customerName))).thenThrow(CustomerNotFoundException.class);
        //ACT
        var response = mvc.perform(
                get("/bookings/customer_name")
                        .param("customerName", customerName)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Should return a code 200 when update a reservation")
    void updateReservationScenario01() throws Exception {
        //ARRANGE
        Long id = 1L;
        BookingUpdateEntryDTO dto = new BookingUpdateEntryDTO(1L, 2L,
                LocalDateTime.of(2025, 11, 07, 22, 00, 00), BookingStatus.CONFIRMED, 1L);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCustomerName(new Customer());
        booking.setRoom(new Room());
        booking.setCheckInDate(dto.checkInDate());
        BDDMockito.when((bookingService.updateReservation(dto, id))).thenReturn(new BookingOutputDTO(booking));
        //ACT
        var response = mvc.perform(
                        patch("/bookings/{id}", id)
                                .content(bookingUpdateEntryDTOJacksonTester.write(dto).getJson())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()
                .getResponse();
        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
        var jsonExpected = bookingOutputDTOJacksonTester.write(new BookingOutputDTO(booking)).getJson();
        Assertions.assertEquals(jsonExpected, response.getContentAsString());
    }

    @Test
    @DisplayName("Should return a code 404 when booking not found when call the method updateReservation")
    void updateReservationScenario02() throws Exception {
        //ARRANGE
        Long id = 1L;
        BookingUpdateEntryDTO dto = new BookingUpdateEntryDTO(1L, 2L, LocalDateTime.of(2025, 11, 07, 22, 00, 00), BookingStatus.CONFIRMED, 1L);
        BDDMockito.when((bookingService.updateReservation(dto, id))).thenThrow(BookingsNotFoundException.class);
        //ACT
        var response = mvc.perform(
                        patch("/bookings/{id}", id)
                                .content(bookingUpdateEntryDTOJacksonTester.write(dto).getJson())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()
                .getResponse();
        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Should return a code 200 when delete a reservation")
    void deleteAReservationScenario01() throws Exception {
        //ARRANGE
        Long id = 1l;
        Booking booking = new Booking();
        booking.setId(id);
        booking.setCustomerName(new Customer());
        booking.setRoom(new Room());
        BDDMockito.when((bookingService.deleteAReservation(id))).thenReturn(new BookingDeleteStatusDTO(booking));
        //ACT
        var response = mvc.perform(
                delete("/bookings/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andReturn().getResponse();
        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
        var jsonExpected = bookingDeleteStatusDTOJacksonTester
                .write(new BookingDeleteStatusDTO(booking)).getJson();
        Assertions.assertEquals(jsonExpected, response.getContentAsString());
    }

    @Test
    @DisplayName("Should return  a successful message and status http 200 when the checkout is updated")
    void updateCheckOutDateScenario01() throws Exception {
        //ARRANGE
        Long id = 1l;
        String message = String.format("{\"message: \":\"Checkout successful!\"}");

//        //ACT
//        bookingService.updateCheckOutDate(id);
        var response = mvc.perform(
                patch("/bookings//checkout/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andReturn().getResponse();
        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(message, response.getContentAsString().toString());
    }
}
package com.challenge.hotel_california.validatorRefactor;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.DTOs.BookingUpdateEntryDTO;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.exceptions.*;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.BookingRepository;
import com.challenge.hotel_california.repository.CustomerRepository;
import com.challenge.hotel_california.repository.RoomRepository;
import com.challenge.hotel_california.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Component
public class ValidatorsBookingsHandler implements IValidatorBookings {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void verifyValidatorsBookings(BookingEntryDTO bookingEntryDTO, Room room, Customer customer) {
        if (!roomRepository.existsById(bookingEntryDTO.roomId())) {
            throw new RoomNotFoundException("Room " + bookingEntryDTO.roomId() + " not found into Database!");
        }
        if (!room.getStatus().equals(RoomStatus.AVAILABLE)) {

            throw new RoomNotAvailableException("Room " + room.getNumber() + " not Available at this moment!");
        }
        if (!customerRepository.existsById(bookingEntryDTO.customerId()) || customer.getIsDeleted()) {
            throw new CustomerNotFoundException("Customer" + bookingEntryDTO.customerId() + " does not exist or has been deleted");
        }

        List<BookingStatus> optionsStatusBooking = Arrays.asList(BookingStatus.CANCELLED, BookingStatus.COMPLETED);
        List<Booking> bookingsRoom = bookingRepository.getBookingsById(bookingEntryDTO.roomId(), optionsStatusBooking);
        if (!bookingsRoom.isEmpty()) {
            throw new BookingsExistsException("There are active bookings for this room.");
        }

    }

    @Override
    public void verifyBookingsUpdateValidators(BookingUpdateEntryDTO bookingUpdateEntryDTO, Room roomFound,
                                               Booking bookingFound, Customer customerFound, Long id) {
        if (!roomRepository.existsById(bookingUpdateEntryDTO.roomId())) {
            throw new RoomNotFoundException("Room " + bookingUpdateEntryDTO.roomId() + " Not Found!");
        }
        if (roomFound.getStatus().equals(RoomStatus.BOOKED) && !roomFound.getId().equals(bookingFound.getRoom().getId())) {
            throw new BookingsExistsException("This room is already booked!");
        }
        if (!bookingRepository.existsById(bookingUpdateEntryDTO.id())) {
            throw new BookingsNotFoundException("Booking not found!");
        }
        if (!bookingFound.getId().equals(bookingUpdateEntryDTO.id())) {
            throw new BookingsNotFoundException("Booking " + id + " not the same of id: " + bookingFound.getId() + " found in database");
        }
        var bookingStatus = bookingFound.getStatus();
        if (bookingStatus.equals(BookingStatus.COMPLETED) || bookingStatus.equals(BookingStatus.CANCELLED)) {
            throw new BookingStatusException("Cannot change a completed or cancelled reservation!");
        }
        // Verifies that the new check-in date is at least 24 hours later than the current check-in date
        LocalDateTime currentCheckInDate = bookingFound.getCheckInDate();
        LocalDateTime newCheckInDate = bookingUpdateEntryDTO.checkInDate();
        if (newCheckInDate.isAfter(currentCheckInDate.plusHours(24)) || currentCheckInDate.equals(newCheckInDate)) {
            bookingFound.setCheckInDate(newCheckInDate);
        } else {
            throw new BookingCheckInDateNotBeforeException("Check-in date can only be changed to at least 24 hours later.");
        }

        if (!customerRepository.existsById(bookingUpdateEntryDTO.customerId()) || customerFound.getIsDeleted()) {
            throw new CustomerNotFoundException("Customer " + bookingUpdateEntryDTO.customerId() + " not exists into Database or has been deleted");
        }
    }
}

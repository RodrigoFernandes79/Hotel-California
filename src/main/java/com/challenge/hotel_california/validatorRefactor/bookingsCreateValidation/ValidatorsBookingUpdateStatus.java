package com.challenge.hotel_california.validatorRefactor.bookingsCreateValidation;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.BookingsExistsException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ValidatorsBookingUpdateStatus implements IValidatorBookings {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void verifyValidatorsBookings(BookingEntryDTO bookingEntryDTO, Room room, Customer customer) {

        List<BookingStatus> optionsStatusBooking = Arrays.asList(BookingStatus.CANCELLED, BookingStatus.COMPLETED);
        List<Booking> bookingsRoom = bookingRepository.getBookingsById(bookingEntryDTO.roomId(), optionsStatusBooking);
        if (!bookingsRoom.isEmpty()) {
            throw new BookingsExistsException("There are active bookings for this room.");
        }

    }


}

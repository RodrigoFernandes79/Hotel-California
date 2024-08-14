package com.challenge.hotel_california.validatorRefactor.rooms;

import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.BookingsExistsException;
import com.challenge.hotel_california.exceptions.NumberRoomFoundException;
import com.challenge.hotel_california.exceptions.RoomNotFoundException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.BookingRepository;
import com.challenge.hotel_california.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ValidateIfBookingsAreActive implements IValidatorRooms {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void verifyRoomUpdateValidators(Long id, RoomEntryUpdateDTO roomEntryUpdateDTO) {
        List<BookingStatus> optionsStatusBooking = Arrays.asList(BookingStatus.CANCELLED, BookingStatus.COMPLETED);
        List<Booking> bookingsRoom = bookingRepository.getBookingsById(id, optionsStatusBooking);
        if (!bookingsRoom.isEmpty()) {
            throw new BookingsExistsException("There are active bookings for this room.");
        }
    }

}

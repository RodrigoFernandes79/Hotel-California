package com.challenge.hotel_california.validatorRefactor.bookingsUpdateValidation;

import com.challenge.hotel_california.DTOs.BookingUpdateEntryDTO;
import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.exceptions.BookingsExistsException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfRoomAlreadyBooked implements IValidatorBookingsUpdate {

    @Override
    public void verifyBookingsUpdateValidators(BookingUpdateEntryDTO bookingUpdateEntryDTO, Room roomFound,
                                               Booking bookingFound, Customer customerFound, Long id) {

        if (roomFound.getStatus().equals(RoomStatus.BOOKED) && !roomFound.getId().equals(bookingFound.getRoom().getId())) {
            throw new BookingsExistsException("This room is already booked!");
        }
    }
}

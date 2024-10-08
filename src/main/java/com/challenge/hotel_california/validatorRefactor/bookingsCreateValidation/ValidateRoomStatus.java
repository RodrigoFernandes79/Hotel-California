package com.challenge.hotel_california.validatorRefactor.bookingsCreateValidation;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.exceptions.RoomNotAvailableException;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import org.springframework.stereotype.Component;


@Component
public class ValidateRoomStatus implements IValidatorBookings {
    @Override
    public void verifyValidatorsBookings(BookingEntryDTO bookingEntryDTO, Room room, Customer customer) {
        if (!room.getStatus().equals(RoomStatus.AVAILABLE)) {

            throw new RoomNotAvailableException("Room " + room.getNumber() + " not Available at this moment!");
        }
    }
}

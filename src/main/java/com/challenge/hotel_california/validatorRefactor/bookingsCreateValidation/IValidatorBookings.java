package com.challenge.hotel_california.validatorRefactor.bookingsCreateValidation;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;

public interface IValidatorBookings {
    void verifyValidatorsBookings(BookingEntryDTO bookingEntryDTO, Room room, Customer customer);

}

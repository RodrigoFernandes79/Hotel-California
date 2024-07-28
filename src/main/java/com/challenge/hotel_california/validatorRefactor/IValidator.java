package com.challenge.hotel_california.validatorRefactor;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.DTOs.CustomerEntryDTO;
import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;

public interface IValidator {
    void verifyCustomersValidators(CustomerEntryDTO customerEntryDTO);

    void verifyValidatorsBookings(BookingEntryDTO bookingEntryDTO, Room room, Customer customer);
    void verifyRoomUpdateValidators(Long id, Room room, RoomEntryUpdateDTO roomEntryUpdateDTO);
}

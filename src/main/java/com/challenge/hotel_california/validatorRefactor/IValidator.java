package com.challenge.hotel_california.validatorRefactor;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.DTOs.BookingUpdateEntryDTO;
import com.challenge.hotel_california.DTOs.CustomerEntryDTO;
import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;

public interface IValidator {
    void verifyCustomersValidators(CustomerEntryDTO customerEntryDTO);

    void verifyValidatorsBookings(BookingEntryDTO bookingEntryDTO, Room room, Customer customer);

    void verifyRoomUpdateValidators(Long id, Room room, RoomEntryUpdateDTO roomEntryUpdateDTO);

    void verifyBookingsUpdateValidators(BookingUpdateEntryDTO bookingUpdateEntryDTO, Room roomFound,
                                        Booking bookingFound, Customer customerFound, Long id);

    void verifyBookingsDeleteValidators(Long id, Booking bookingFound);
    void verifyBookingsUpdateCheckOutValidators(Long id, Booking bookingFound);

}

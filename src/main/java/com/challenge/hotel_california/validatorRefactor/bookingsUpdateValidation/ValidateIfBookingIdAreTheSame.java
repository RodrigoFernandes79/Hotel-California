package com.challenge.hotel_california.validatorRefactor.bookingsUpdateValidation;

import com.challenge.hotel_california.DTOs.BookingUpdateEntryDTO;
import com.challenge.hotel_california.exceptions.BookingsNotFoundException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfBookingIdAreTheSame implements IValidatorBookingsUpdate {
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void verifyBookingsUpdateValidators(BookingUpdateEntryDTO bookingUpdateEntryDTO, Room roomFound,
                                               Booking bookingFound, Customer customerFound, Long id) {
        if (!bookingFound.getId().equals(bookingUpdateEntryDTO.id())) {
            throw new BookingsNotFoundException("Booking " + id + " not the same of id: " + bookingUpdateEntryDTO.id() + " found in database");
        }
    }

}

package com.challenge.hotel_california.validatorRefactor.bookingsUpdateValidation;

import com.challenge.hotel_california.DTOs.BookingUpdateEntryDTO;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.BookingStatusException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateIBookingStatus implements IValidatorBookingsUpdate {
    @Autowired
    private BookingRepository bookingRepository;


    @Override
    public void verifyBookingsUpdateValidators(BookingUpdateEntryDTO bookingUpdateEntryDTO, Room roomFound,
                                               Booking bookingFound, Customer customerFound, Long id) {
        var bookingStatus = bookingFound.getStatus();
        if (bookingStatus.equals(BookingStatus.COMPLETED) || bookingStatus.equals(BookingStatus.CANCELLED)) {
            throw new BookingStatusException("Cannot change a completed or cancelled reservation!");
        }
    }
}

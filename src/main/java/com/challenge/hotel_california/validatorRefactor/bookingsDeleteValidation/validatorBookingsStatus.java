package com.challenge.hotel_california.validatorRefactor.bookingsDeleteValidation;

import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.BookingStatusException;
import com.challenge.hotel_california.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class validatorBookingsStatus implements IValidatorBookingsDelete {
    @Override
    public void verifyBookingsDeleteValidators(Long id, Booking bookingFound) {
        var bookingStatusFound = bookingFound.getStatus();
        if (bookingStatusFound.equals(BookingStatus.CHECKED_IN)) {
            throw new BookingStatusException("This Reservation cannot be cancelled case its made a Check in!");
        }
        if (bookingStatusFound.equals(BookingStatus.COMPLETED) || bookingStatusFound.equals(BookingStatus.CANCELLED)) {
            throw new BookingStatusException("Cannot cancel a completed or cancelled reservation!");
        }

    }
}

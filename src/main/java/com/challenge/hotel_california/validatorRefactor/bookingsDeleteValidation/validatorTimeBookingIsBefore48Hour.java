package com.challenge.hotel_california.validatorRefactor.bookingsDeleteValidation;

import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.BookingStatusException;
import com.challenge.hotel_california.model.Booking;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class validatorTimeBookingIsBefore48Hour implements IValidatorBookingsDelete {

    @Override
    public void verifyBookingsDeleteValidators(Long id, Booking bookingFound) {
        var bookingStatusFound = bookingFound.getStatus();
        if (bookingStatusFound.equals(BookingStatus.CONFIRMED)) {
            var checkInCurrentDate = bookingFound.getCheckInDate();
            var cancelDate = LocalDateTime.now();
            var differenceInHours = Duration.between(cancelDate, checkInCurrentDate).toHours();
            if (differenceInHours < 48) {
                throw new BookingStatusException("Cannot cancel a reservation under 48h of check-in Date");
            }
        }
    }
}

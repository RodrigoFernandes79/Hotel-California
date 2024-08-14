package com.challenge.hotel_california.validatorRefactor.bookingsUpdateCheckoutValidation;

import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.RoomNotAvailableException;
import com.challenge.hotel_california.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class ValidateBookingStatus implements IValidatorBookingsCheckout {
    @Override
    public void verifyBookingsCheckoutValidators(Long id, Booking bookingFound) {
        if (bookingFound.getStatus().equals(BookingStatus.COMPLETED) || bookingFound.getStatus().equals(BookingStatus.CANCELLED)) {
            throw new RoomNotAvailableException("Cannot cancel a completed or cancelled reservation!");
        }
    }
}

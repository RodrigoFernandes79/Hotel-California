package com.challenge.hotel_california.validatorRefactor.bookingsUpdateCheckoutValidation;

import com.challenge.hotel_california.exceptions.BookingsNotFoundException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfBookingsExistsById implements IValidatorBookingsCheckout {
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void verifyBookingsCheckoutValidators(Long id, Booking bookingFound) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingsNotFoundException("No Booking was found");

        }
    }
}

package com.challenge.hotel_california.validatorRefactor.bookingsUpdateCheckoutValidation;

import com.challenge.hotel_california.model.Booking;

public interface IValidatorBookingsCheckout {
    void verifyBookingsCheckoutValidators(Long id, Booking bookingFound);
}

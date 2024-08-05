package com.challenge.hotel_california.validatorRefactor;

import com.challenge.hotel_california.model.Booking;

public interface IValidatorBookingsDeleteAndCheckout {
    void verifyBookingsValidators(Long id, Booking bookingFound);
}

package com.challenge.hotel_california.validatorRefactor.bookingsDeleteValidation;

import com.challenge.hotel_california.model.Booking;

public interface IValidatorBookingsDelete {
    void verifyBookingsDeleteValidators(Long id, Booking bookingFound);
}

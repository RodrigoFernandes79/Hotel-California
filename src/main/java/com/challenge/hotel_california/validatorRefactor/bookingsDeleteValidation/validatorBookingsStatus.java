package com.challenge.hotel_california.validatorRefactor.bookingsDeleteValidation;

import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.exceptions.BookingStatusException;
import com.challenge.hotel_california.exceptions.BookingsNotFoundException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.repository.BookingRepository;
import com.challenge.hotel_california.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

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

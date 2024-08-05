package com.challenge.hotel_california.validatorRefactor;

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
public class validatorBookingsDelete implements IValidatorBookingsDeleteAndCheckout {
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void verifyBookingsValidators(Long id, Booking bookingFound) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingsNotFoundException("Booking not found");
        }
        var bookingStatusFound = bookingFound.getStatus();
        if (bookingStatusFound.equals(BookingStatus.CHECKED_IN)) {
            throw new BookingStatusException("This Reservation cannot be cancelled case its made a Check in!");
        }
        if (bookingStatusFound.equals(BookingStatus.COMPLETED) || bookingStatusFound.equals(BookingStatus.CANCELLED)) {
            throw new BookingStatusException("Cannot cancel a completed or cancelled reservation!");
        }
        if (bookingStatusFound.equals(BookingStatus.CONFIRMED)) {
            var checkInCurrentDate = bookingFound.getCheckInDate();
            var cancelDate = LocalDateTime.now();
            var differenceInHours = Duration.between(cancelDate, checkInCurrentDate).toHours();
            if (differenceInHours < 48) {
                throw new BookingStatusException("Cannot cancel a reservation under 48h of check-in Date");
            }
            bookingFound.setStatus(BookingStatus.CANCELLED);
            bookingFound.getRoom().setStatus(RoomStatus.AVAILABLE);
            BookingService bookingService = new BookingService();
            bookingService.calculateTax(bookingFound, null);
        }
    }
}

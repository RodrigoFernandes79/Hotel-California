package com.challenge.hotel_california.validatorRefactor.bookingsUpdateValidation;

import com.challenge.hotel_california.DTOs.BookingUpdateEntryDTO;
import com.challenge.hotel_california.exceptions.BookingCheckInDateNotBeforeException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidateIfCheckInDateIsChangeAfter24Hour implements IValidatorBookingsUpdate {

    @Override
    public void verifyBookingsUpdateValidators(BookingUpdateEntryDTO bookingUpdateEntryDTO, Room roomFound,
                                               Booking bookingFound, Customer customerFound, Long id) {
        // Verifies that the new check-in date is at least 24 hours later than the current check-in date
        LocalDateTime currentCheckInDate = bookingFound.getCheckInDate();
        LocalDateTime newCheckInDate = bookingUpdateEntryDTO.checkInDate();
        if (newCheckInDate.isAfter(currentCheckInDate.plusHours(24)) || currentCheckInDate.equals(newCheckInDate)) {
            bookingFound.setCheckInDate(newCheckInDate);
        } else {
            throw new BookingCheckInDateNotBeforeException("Check-in date can only be changed to at least 24 hours later.");
        }
    }
}

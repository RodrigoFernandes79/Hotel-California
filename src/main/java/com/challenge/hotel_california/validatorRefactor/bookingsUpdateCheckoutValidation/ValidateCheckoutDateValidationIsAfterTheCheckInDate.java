package com.challenge.hotel_california.validatorRefactor.bookingsUpdateCheckoutValidation;

import com.challenge.hotel_california.exceptions.BookingCheckInDateNotBeforeException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
class ValidateCheckoutDateValidationIsAfterTheCheckInDate implements IValidatorBookingsCheckout {
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void verifyBookingsCheckoutValidators(Long id, Booking bookingFound) {
        LocalTime checkInTime = LocalTime.of(14, 0);  // 14:00
        LocalTime checkOutTime = LocalTime.of(8, 0);  // 08:00

        var checkInDate = bookingFound.getCheckInDate().with(checkInTime);
        var checkoutDate = LocalDateTime.now();
        var endOfCheckoutDate = checkoutDate.with(checkOutTime);
        var totalPrice = bookingFound.getRoom().getPrice();

        var differenceInDays = Duration.between(checkInDate, checkoutDate).toDays();

        if (differenceInDays < 0) {
            throw new BookingCheckInDateNotBeforeException("Check out date cannot be before the check in date");

        } else if (differenceInDays == 0) {
            bookingFound.setTotalPrice(totalPrice);

        } else {
            if (checkoutDate.isAfter(endOfCheckoutDate)) {
                bookingFound.setTotalPrice(totalPrice.multiply(BigDecimal.valueOf(differenceInDays)).add(totalPrice));
            } else {
                bookingFound.setTotalPrice(totalPrice.multiply(BigDecimal.valueOf(differenceInDays)));
            }
        }
    }
}

package com.challenge.hotel_california.validatorRefactor.bookingsUpdateCheckoutValidation;

import com.challenge.hotel_california.exceptions.BookingCheckInDateNotBeforeException;
import com.challenge.hotel_california.model.Booking;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
class ValidateCheckoutDateIsAfterTheCheckInDate implements IValidatorBookingsCheckout {

    @Override
    public void verifyBookingsCheckoutValidators(Long id, Booking bookingFound) {
        LocalTime checkInTime = LocalTime.of(14, 0);  // 14:00
        LocalTime checkOutTime = LocalTime.of(8, 0);  // 08:00

        var checkInDate = bookingFound.getCheckInDate().with(checkInTime);
        var checkoutDate = LocalDateTime.now();
        var expectedCheckoutDate = checkoutDate.with(checkOutTime);
        var totalPrice = bookingFound.getRoom().getPrice();

        if (!checkInDate.isBefore(checkoutDate)) {
            throw new BookingCheckInDateNotBeforeException("Check out date cannot be before the check in date");
        }

        var dailyDifference = Duration.between(checkInDate, checkoutDate);
        var dailyQuantity = Math.ceil(dailyDifference.toHours() / 18);// 18 is the difference hours to a daily from 14:00 to 8:00
        if (dailyDifference.toHours() <= 18) {
            bookingFound.setTotalPrice(totalPrice);
        } else if (dailyDifference.toHours() > 18) {
            if (checkoutDate.isAfter(expectedCheckoutDate)) {
                bookingFound.setTotalPrice(totalPrice.multiply(BigDecimal.valueOf(dailyQuantity + 1)));// sum one daily
            } else {
                bookingFound.setTotalPrice(totalPrice.multiply(BigDecimal.valueOf(dailyQuantity)));
            }
        }
    }
}

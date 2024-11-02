package com.challenge.hotel_california.validatorRefactor.bookingsUpdateCheckoutValidation;

import com.challenge.hotel_california.exceptions.BookingCheckInDateNotBeforeException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Room;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ExtendWith(MockitoExtension.class)
class ValidateCheckoutDateIsAfterTheCheckInDateTest {
    @InjectMocks
    private ValidateCheckoutDateIsAfterTheCheckInDate validateCheckoutDateValidationIsAfterTheCheckInDate;
    @Mock
    private Booking bookingFound;

    private Room room = new Room();

    @Test
    @DisplayName("Verify if check in date is after check out date and  throws exception")
    void verifyBookingsCheckoutValidatorsScenario01() {
        LocalTime checkInTime = LocalTime.of(14, 0);  // 14:00
        LocalDateTime checkInDate = LocalDateTime.now().plusDays(5).with(checkInTime);
        LocalDateTime checkoutDate = checkInDate.minusMinutes(1);

        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(checkInDate);
        BDDMockito.given(bookingFound.getCheckOutDate()).willReturn(checkoutDate);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);

        Assertions.assertFalse(checkInDate.isBefore(checkoutDate));
        Assertions.assertThrows(BookingCheckInDateNotBeforeException.class, () -> validateCheckoutDateValidationIsAfterTheCheckInDate
                .verifyBookingsCheckoutValidators(null, bookingFound));
    }

    @Test
    @DisplayName("Verify if daily is less than 18 hours check in date and not throws exception")
    void verifyBookingsCheckoutValidators() {
        //arrange
        BigDecimal totalPrice = BigDecimal.valueOf(200.00);
        room.setPrice(totalPrice);

        LocalTime checkInTime = LocalTime.of(14, 0);  // 14:00

        LocalDateTime checkInDate = LocalDateTime.now().minusDays(1).with(checkInTime);
        LocalDateTime checkoutDate = checkInDate.plusHours(17);
        var daily = Duration.between(checkInDate, checkoutDate);

        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(checkInDate);
        BDDMockito.given(bookingFound.getCheckOutDate()).willReturn(checkoutDate);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);
        //Act & Assert
        Assertions.assertDoesNotThrow(() -> validateCheckoutDateValidationIsAfterTheCheckInDate
                .verifyBookingsCheckoutValidators(null, bookingFound));

        BDDMockito.then(bookingFound).should().setTotalPrice(room.getPrice());

        Assertions.assertEquals(17, daily.toHours());
        Assertions.assertEquals(BigDecimal.valueOf(200.00), totalPrice);
        Assertions.assertTrue(checkInDate.isBefore(checkoutDate));
//        }
    }

    @Test
    @DisplayName("Verify if correct totalPrice is set when checkoutDate is after endOfCheckoutDate")
    void verifyBookingsCheckoutValidatorsScenario03() {
        //Arrange
        var totalPrice = BigDecimal.valueOf(200);
        room.setPrice(totalPrice);
        LocalTime checkInTime = LocalTime.of(14, 0);  // 14:00
        LocalDateTime checkInDate = LocalDateTime.now().minusDays(1).with(checkInTime);
        LocalDateTime checkoutDate = checkInDate.plusHours(17);

        var endOfCheckoutDate = checkoutDate.minusDays(2);
        var dailyDifference = Duration.between(checkInDate, checkoutDate);
        var dailyQuantity = Math.ceil(dailyDifference.toHours() / 18);
        BigDecimal expectedTotalPrice = totalPrice.multiply(BigDecimal.valueOf(dailyQuantity + 1));

        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(checkInDate);
        BDDMockito.given(bookingFound.getCheckOutDate()).willReturn(checkoutDate);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);
        //Act
        validateCheckoutDateValidationIsAfterTheCheckInDate.verifyBookingsCheckoutValidators(null, bookingFound);
        //Assertions
        Assertions.assertEquals(17, dailyDifference.toHours());
        Assertions.assertEquals(BigDecimal.valueOf(200.0), expectedTotalPrice);
        Assertions.assertTrue(checkoutDate.isAfter(endOfCheckoutDate));

    }

    @Test
    @DisplayName("Verify if correct totalPrice is set when checkoutDate is not after endOfCheckoutDate")
    void verifyBookingsCheckoutValidatorsScenario04() {
        var totalPrice = BigDecimal.valueOf(200);
        room.setPrice(totalPrice);
        LocalTime checkInTime = LocalTime.of(14, 0);  // 14:00
        LocalTime checkOutTime = LocalTime.of(8, 0);  // 08:00
        LocalDateTime checkoutDate = LocalDateTime.now().withHour(6);
        LocalDateTime checkInDate = checkoutDate.minusDays(2).with(checkInTime);

        var endOfCheckoutDate = checkoutDate.with(checkOutTime);
        var dailyDifference = Duration.between(checkInDate, checkoutDate);
        var dailyQuantity = Math.ceil(dailyDifference.toHours() / 18);
        BigDecimal expectedTotalPrice = totalPrice.multiply(BigDecimal.valueOf(dailyQuantity));


        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(checkInDate);
        BDDMockito.given(bookingFound.getCheckOutDate()).willReturn(checkoutDate);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);

        validateCheckoutDateValidationIsAfterTheCheckInDate.verifyBookingsCheckoutValidators(null, bookingFound);
        Assertions.assertFalse(checkoutDate.isAfter(endOfCheckoutDate));
        BDDMockito.then(bookingFound).should().setTotalPrice(expectedTotalPrice);
        Assertions.assertEquals(40, dailyDifference.toHours());
        Assertions.assertEquals(BigDecimal.valueOf(400.00), expectedTotalPrice);
        Assertions.assertFalse(checkoutDate.isAfter(endOfCheckoutDate));
    }

}
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
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.Mockito.mockStatic;

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
        LocalDateTime checkInDate = LocalDateTime.of(2024, 8, 26, 7, 00, 00).with(checkInTime);
        LocalDateTime checkoutDate = LocalDateTime.of(2024, 8, 26, 9, 59, 00);
        try (MockedStatic<LocalDateTime> localDateTimeMocked = mockStatic(LocalDateTime.class)) {
            localDateTimeMocked.when(LocalDateTime::now).thenReturn(checkoutDate);
        }

        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(checkInDate);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);

        Assertions.assertFalse(checkInDate.isBefore(checkoutDate));
        Assertions.assertThrows(BookingCheckInDateNotBeforeException.class, () -> validateCheckoutDateValidationIsAfterTheCheckInDate
                .verifyBookingsCheckoutValidators(null, bookingFound));
    }

    @Test
    @DisplayName("Verify if daily is less than 18 hours check in date and not throws exception")
    void verifyBookingsCheckoutValidators() {
        BigDecimal totalPrice = BigDecimal.valueOf(200);
        room.setPrice(totalPrice);
        LocalTime checkInTime = LocalTime.of(14, 0);  // 14:00
        LocalDateTime checkInDate = LocalDateTime.of(2024, 8, 25, 8, 00, 00).with(checkInTime);
        LocalDateTime checkoutDate = LocalDateTime.of(2024, 8, 26, 7, 00, 00);
        try (MockedStatic<LocalDateTime> localDateTimeMocked = mockStatic(LocalDateTime.class)) {
            localDateTimeMocked.when(LocalDateTime::now).thenReturn(checkoutDate);
        }

        var daily = Duration.between(checkInDate, checkoutDate);

        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(checkInDate);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);


        Assertions.assertDoesNotThrow(() -> validateCheckoutDateValidationIsAfterTheCheckInDate
                .verifyBookingsCheckoutValidators(null, bookingFound));
        BDDMockito.then(bookingFound).should().setTotalPrice(totalPrice);

        Assertions.assertEquals(17, daily.toHours());
        Assertions.assertEquals(BigDecimal.valueOf(200), totalPrice);
        Assertions.assertTrue(checkInDate.isBefore(checkoutDate));

    }

    @Test
    @DisplayName("Verify if correct totalPrice is set when checkoutDate is after endOfCheckoutDate")
    void verifyBookingsCheckoutValidatorsScenario03() {
        var totalPrice = BigDecimal.valueOf(200);
        room.setPrice(totalPrice);
        LocalTime checkInTime = LocalTime.of(14, 0);  // 14:00
        LocalTime checkOutTime = LocalTime.of(8, 0);
        LocalDateTime checkInDate = LocalDateTime.of(2024, 8, 24, 6, 00, 00).with(checkInTime);
        LocalDateTime checkoutDate = LocalDateTime.of(2024, 8, 26, 16, 00, 00);
        var endOfCheckoutDate = checkoutDate.with(checkOutTime);
        var dailyDifference = Duration.between(checkInDate, checkoutDate);
        var dailyQuantity = Math.ceil(dailyDifference.toHours() / 18);

        try (MockedStatic<LocalDateTime> localDateTimeMocked = mockStatic(LocalDateTime.class)) {
            localDateTimeMocked.when(LocalDateTime::now).thenReturn(checkoutDate);
        }
        BigDecimal expectedTotalPrice = totalPrice.multiply(BigDecimal.valueOf(dailyQuantity + 1));
        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(checkInDate);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);

        validateCheckoutDateValidationIsAfterTheCheckInDate.verifyBookingsCheckoutValidators(null, bookingFound);
        BDDMockito.then(bookingFound).should().setTotalPrice(expectedTotalPrice);
        Assertions.assertEquals(50, dailyDifference.toHours());
        Assertions.assertEquals(BigDecimal.valueOf(600.0), expectedTotalPrice);
        Assertions.assertTrue(checkoutDate.isAfter(endOfCheckoutDate));

    }

    @Test
    @DisplayName("Verify if correct totalPrice is set when checkoutDate is not after endOfCheckoutDate")
    void verifyBookingsCheckoutValidatorsScenario04() {
        var totalPrice = BigDecimal.valueOf(200);
        room.setPrice(totalPrice);
        LocalTime checkInTime = LocalTime.of(14, 0);  // 14:00
        LocalTime checkOutTime = LocalTime.of(8, 0);
        LocalDateTime checkInDate = LocalDateTime.of(2024, 8, 24, 6, 00, 00).with(checkInTime);
        LocalDateTime checkoutDate = LocalDateTime.of(2024, 8, 26, 7, 00, 00);
        var endOfCheckoutDate = checkoutDate.with(checkOutTime);
        var dailyDifference = Duration.between(checkInDate, checkoutDate);
        var dailyQuantity = Math.ceil(dailyDifference.toHours() / 18);


        try (MockedStatic<LocalDateTime> localDateTimeMocked = mockStatic(LocalDateTime.class)) {
            localDateTimeMocked.when(LocalDateTime::now).thenReturn(checkoutDate);
        }
        BigDecimal expectedTotalPrice = totalPrice.multiply(BigDecimal.valueOf(dailyQuantity));
        BDDMockito.given(bookingFound.getCheckInDate()).willReturn(checkInDate);
        BDDMockito.given(bookingFound.getRoom()).willReturn(room);

        validateCheckoutDateValidationIsAfterTheCheckInDate.verifyBookingsCheckoutValidators(null, bookingFound);
        BDDMockito.then(bookingFound).should().setTotalPrice(expectedTotalPrice);
        Assertions.assertEquals(41, dailyDifference.toHours());
        Assertions.assertEquals(BigDecimal.valueOf(400.0), expectedTotalPrice);
        Assertions.assertFalse(checkoutDate.isAfter(endOfCheckoutDate));
    }

}
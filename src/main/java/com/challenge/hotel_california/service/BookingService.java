package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.*;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.exceptions.BookingsNotFoundException;
import com.challenge.hotel_california.exceptions.CustomerNotFoundException;
import com.challenge.hotel_california.exceptions.RoomNotFoundException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.BookingRepository;
import com.challenge.hotel_california.repository.CustomerRepository;
import com.challenge.hotel_california.repository.RoomRepository;
import com.challenge.hotel_california.validatorRefactor.bookingsCreateValidation.IValidatorBookings;
import com.challenge.hotel_california.validatorRefactor.bookingsDeleteValidation.IValidatorBookingsDelete;
import com.challenge.hotel_california.validatorRefactor.bookingsUpdateCheckoutValidation.IValidatorBookingsCheckout;
import com.challenge.hotel_california.validatorRefactor.bookingsUpdateValidation.IValidatorBookingsUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private List<IValidatorBookings> verifyValidators;
    @Autowired
    private List<IValidatorBookingsUpdate> verifyUpdateBookingsValidators;
    @Autowired
    private List<IValidatorBookingsDelete> verifyDeleteBookingsValidators;
    @Autowired
    private List<IValidatorBookingsCheckout> verifyCheckoutValidators;


    public Booking createAReservation(BookingEntryDTO bookingEntryDTO) {
        if (!roomRepository.existsById(bookingEntryDTO.roomId())) {
            throw new RoomNotFoundException("Room " + bookingEntryDTO.roomId() + " not found into Database!");
        }
        Room room = roomRepository.getReferenceById(bookingEntryDTO.roomId());
        Customer customer = customerRepository.getReferenceById(bookingEntryDTO.customerId());

        verifyValidators.forEach(v -> v.verifyValidatorsBookings(bookingEntryDTO, room, customer));
        room.setStatus(RoomStatus.BOOKED);
        Booking booking = new Booking(customer, bookingEntryDTO.checkInDate(), room, room.getPrice());//check in is always as 14 o clock
        return bookingRepository.save(booking);
    }

    public Page<BookingOutputListDTO> listAllReservations(Pageable pageable) {
        Page<Booking> bookingsFound = bookingRepository.findAll(pageable);
        if (bookingsFound.isEmpty()) {
            throw new BookingsNotFoundException("No Bookings was found into Database!");
        }
        return bookingsFound.map(BookingOutputListDTO::new);
    }

    public Page<BookingOutputListDTO> listAllReservationsByCustomer(Pageable pageable, String customerName) {
        List<Customer> customerFound = customerRepository.findByNameContainingIgnoreCase(customerName);
        if (customerFound.isEmpty()) {
            throw new CustomerNotFoundException("Customer " + customerName + " not found!");
        }
        Page<Booking> bookingsFound = bookingRepository.findAllByCustomerNameById(pageable, customerName);
        if (bookingsFound.isEmpty()) {
            throw new BookingsNotFoundException("No Bookings was found into Database!");
        }
        return bookingsFound.map(BookingOutputListDTO::new);
    }

    public BookingOutputDTO updateReservation(BookingUpdateEntryDTO bookingUpdateEntryDTO, Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingsNotFoundException("No Booking was found");
        }

        Booking bookingFound = bookingRepository.getReferenceById(id);
        Customer customerFound = customerRepository.getReferenceById(bookingUpdateEntryDTO.customerId());
        Room roomFound = roomRepository.getReferenceById(bookingUpdateEntryDTO.roomId());

        verifyUpdateBookingsValidators.forEach(v -> v.verifyBookingsUpdateValidators(bookingUpdateEntryDTO,
                roomFound, bookingFound, customerFound, id));


        bookingFound.updateBooking(customerFound, bookingUpdateEntryDTO, roomFound);
        calculateTax(bookingFound, bookingUpdateEntryDTO);
        bookingRepository.save(bookingFound);
        return new BookingOutputDTO(bookingFound);
    }

    public BookingDeleteStatusDTO deleteAReservation(long id) {
        Booking bookingFound = bookingRepository.getReferenceById(id);

        verifyDeleteBookingsValidators.forEach(v -> v.verifyBookingsDeleteValidators(id, bookingFound));
        bookingFound.setStatus(BookingStatus.CANCELLED);
        bookingFound.getRoom().setStatus(RoomStatus.AVAILABLE);
        calculateTax(bookingFound);
        return new BookingDeleteStatusDTO(bookingFound);

    }

    public void updateCheckOutDate(Long id) {
        Booking bookingFound = bookingRepository.getReferenceById(id);

        var checkoutDate = LocalDateTime.now();
        bookingFound.setCheckOutDate(checkoutDate);

        verifyCheckoutValidators.forEach(v -> v.verifyBookingsCheckoutValidators(id, bookingFound));

        bookingFound.getRoom().setStatus(RoomStatus.AVAILABLE);
        bookingFound.setStatus(BookingStatus.COMPLETED);


    }

    private void calculateTax(Booking bookingFound, BookingUpdateEntryDTO bookingUpdateEntryDTO) {
        BigDecimal totalPrice = bookingFound.getRoom().getPrice();

        if (bookingFound.getStatus().equals(BookingStatus.CANCELLED)) {
            // Applies a 20% cancellation fee
            totalPrice = totalPrice.multiply(BigDecimal.valueOf(0.20));
        } else if (bookingUpdateEntryDTO.checkInDate() != null &&
                bookingFound.getCheckInDate().isEqual(bookingUpdateEntryDTO.checkInDate())) {
            // Applies a 20% change fee plus the room price if the check-in date is changed
            totalPrice = totalPrice.multiply(BigDecimal.valueOf(1.20));
        }

        // Rounds to two decimal places
        totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP);

        bookingFound.setTotalPrice(totalPrice);
    }
    private void calculateTax(Booking bookingFound) {
        BigDecimal totalPrice = bookingFound.getRoom().getPrice();

        if (bookingFound.getStatus().equals(BookingStatus.CANCELLED)) {
            // Applies a 20% cancellation fee
            totalPrice = totalPrice.multiply(BigDecimal.valueOf(0.20));
        }

        // Rounds to two decimal places
        totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP);

        bookingFound.setTotalPrice(totalPrice);
    }
}

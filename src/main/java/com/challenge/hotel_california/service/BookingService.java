package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.*;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.exceptions.BookingsNotFoundException;
import com.challenge.hotel_california.exceptions.CustomerNotFoundException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.BookingRepository;
import com.challenge.hotel_california.repository.CustomerRepository;
import com.challenge.hotel_california.repository.RoomRepository;
import com.challenge.hotel_california.validatorRefactor.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    private List<IValidator> verifyValidators;

    public Booking createAReservation(BookingEntryDTO bookingEntryDTO) {
        Room room = roomRepository.getReferenceById(bookingEntryDTO.roomId());
        Customer customer = customerRepository.getReferenceById(bookingEntryDTO.customerId());

        verifyValidators.forEach(v -> v.verifyValidatorsBookings(bookingEntryDTO, room, customer));
        room.setStatus(RoomStatus.BOOKED);
        Booking booking = new Booking(customer, bookingEntryDTO.checkInDate(), room, room.getPrice());//check in is always as 14 o clock
        return bookingRepository.save(booking);
    }

    public ResponseEntity<Page<BookingOutputListDTO>> listAllReservations(Pageable pageable) {
        Page<Booking> bookingsFound = bookingRepository.findAll(pageable);
        if (bookingsFound.isEmpty()) {
            throw new BookingsNotFoundException("No Bookings was found into Database!");
        }
        return ResponseEntity.ok().body(bookingsFound.map(BookingOutputListDTO::new));
    }

    public ResponseEntity<Page<BookingOutputListDTO>> listAllReservationsByCustomer(Pageable pageable, String customerName) {
        List<Customer> customerFound = customerRepository.findByNameContainingIgnoreCase(customerName);
        if (customerFound.isEmpty()) {
            throw new CustomerNotFoundException("Customer " + customerName + " not found!");
        }
        Page<Booking> bookingsFound = bookingRepository.findAllByCustomerNameById(pageable, customerName);
        if (bookingsFound.isEmpty()) {
            throw new BookingsNotFoundException("No Bookings was found into Database!");
        }

        return ResponseEntity.ok().body(bookingsFound.map(BookingOutputListDTO::new));
    }

    public BookingOutputDTO updateReservation(BookingUpdateEntryDTO bookingUpdateEntryDTO, Long id) {

        Room roomFound = roomRepository.getReferenceById(bookingUpdateEntryDTO.roomId());
        Customer customerFound = customerRepository.getReferenceById(bookingUpdateEntryDTO.customerId());
        Booking bookingFound = bookingRepository.getReferenceById(id);

        verifyValidators.forEach(v -> v.verifyBookingsUpdateValidators(bookingUpdateEntryDTO, roomFound,
                bookingFound, customerFound, id));

        bookingFound.updateBooking(customerFound, bookingUpdateEntryDTO, roomFound);
        calculateTax(bookingFound, bookingUpdateEntryDTO);

        return new BookingOutputDTO(bookingRepository.save(bookingFound));
    }

    public BookingDeleteStatusDTO deleteAReservation(long id) {
        Booking bookingFound = bookingRepository.getReferenceById(id);

        verifyValidators.forEach(v -> v.verifyBookingsDeleteValidators(id, bookingFound));
        bookingFound.setStatus(BookingStatus.CANCELLED);
        bookingFound.getRoom().setStatus(RoomStatus.AVAILABLE);

        return new BookingDeleteStatusDTO(bookingFound);

    }

    public void updateCheckOutDate(Long id) {
        Booking bookingFound = bookingRepository.getReferenceById(id);

        verifyValidators.forEach(v -> v.verifyBookingsUpdateCheckOutValidators(id, bookingFound));
        var checkoutDate = LocalDateTime.now();

        bookingFound.setCheckOutDate(checkoutDate);
        bookingFound.getRoom().setStatus(RoomStatus.AVAILABLE);
        bookingFound.setStatus(BookingStatus.COMPLETED);


    }

    public void calculateTax(Booking bookingFound, BookingUpdateEntryDTO bookingUpdateEntryDTO) {
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

}

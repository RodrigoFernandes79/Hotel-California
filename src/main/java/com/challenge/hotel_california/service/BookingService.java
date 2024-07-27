package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.DTOs.BookingOutputListDTO;
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

import java.util.List;
import java.util.Optional;

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
        Booking booking = new Booking(customer, bookingEntryDTO.checkInDate().withHour(14), room, room.getPrice());//check in is always as 14 o clock
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
        Optional customer = customerRepository.findByNameContainingIgnoreCase(customerName);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer " + customerName + " not found!");
        }
        Page<Booking> bookingsFound = bookingRepository.findAllByCustomerNameById(pageable, customerName);
        if (bookingsFound.isEmpty()) {
            throw new BookingsNotFoundException("No Bookings was found into Database!");
        }

        return ResponseEntity.ok().body(bookingsFound.map(BookingOutputListDTO::new));
    }
}

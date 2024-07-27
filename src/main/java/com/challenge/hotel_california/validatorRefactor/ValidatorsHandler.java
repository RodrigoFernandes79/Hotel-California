package com.challenge.hotel_california.validatorRefactor;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.DTOs.CustomerEntryDTO;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.exceptions.*;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.BookingRepository;
import com.challenge.hotel_california.repository.CustomerRepository;
import com.challenge.hotel_california.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class ValidatorsHandler implements IValidator {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void verifyCustomersValidators(CustomerEntryDTO customerEntryDTO) {
        Optional foundCustomerName = customerRepository.findByName(customerEntryDTO.name());
        if (foundCustomerName.isPresent()) {
            throw new CustomerExistsException("Customer " + customerEntryDTO.name() + " already exists in Database!");
        }
        Optional foundCustomerEmail = customerRepository.findByEmail(customerEntryDTO.email());
        if (foundCustomerEmail.isPresent()) {
            throw new CustomerExistsException("Email " + customerEntryDTO.email() + " already exists in Database!");
        }
        Optional foundCustomerPhone = customerRepository.findByPhone(customerEntryDTO.phone());
        if (foundCustomerPhone.isPresent()) {
            throw new CustomerExistsException("Phone " + customerEntryDTO.phone() + " already exists in Database!");
        }
    }

    @Override
    public void verifyValidatorsBookings(BookingEntryDTO bookingEntryDTO, Room room, Customer customer) {
        if (!roomRepository.existsById(bookingEntryDTO.roomId())) {
            throw new RoomNotFoundException("Room " + bookingEntryDTO.roomId() + " not found into Database!");
        }
        if (!room.getStatus().equals(RoomStatus.AVAILABLE)) {

            throw new RoomNotAvailableException("Room " + room.getNumber() + " not Available at this moment!");
        }
        if (!customerRepository.existsById(bookingEntryDTO.customerId()) || customer.getIsDeleted()) {
            throw new CustomerNotFoundException("Customer" + bookingEntryDTO.customerId() + " does not exist or has been deleted");
        }

        List<BookingStatus> optionsStatusBooking = Arrays.asList(BookingStatus.CANCELLED, BookingStatus.COMPLETED);
        List<Booking> bookingsRoom = bookingRepository.getBookingsById(bookingEntryDTO.roomId(), optionsStatusBooking);
        if (!bookingsRoom.isEmpty()) {
            throw new BookingsExistsException("There are active bookings for this room.");
        }

    }

}

package com.challenge.hotel_california.validatorRefactor.bookingsCreateValidation;

import com.challenge.hotel_california.DTOs.BookingEntryDTO;
import com.challenge.hotel_california.exceptions.CustomerNotFoundException;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfUpdateCustomerExistsOrDeleted implements IValidatorBookings {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void verifyValidatorsBookings(BookingEntryDTO bookingEntryDTO, Room room, Customer customer) {
        if (!customerRepository.existsById(bookingEntryDTO.customerId()) || customer.getIsDeleted()) {
            throw new CustomerNotFoundException("Customer " + bookingEntryDTO.customerId() + " does not exist or has been deleted");
        }
    }
}

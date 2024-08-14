package com.challenge.hotel_california.validatorRefactor.bookingsUpdateValidation;

import com.challenge.hotel_california.DTOs.BookingUpdateEntryDTO;
import com.challenge.hotel_california.exceptions.CustomerNotFoundException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.model.Room;
import com.challenge.hotel_california.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfCustomerExistsByIdOrDeleted implements IValidatorBookingsUpdate {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void verifyBookingsUpdateValidators(BookingUpdateEntryDTO bookingUpdateEntryDTO, Room roomFound,
                                               Booking bookingFound, Customer customerFound, Long id) {

        if (!customerRepository.existsById(bookingUpdateEntryDTO.customerId()) || customerFound.getIsDeleted()) {
            throw new CustomerNotFoundException("Customer " + bookingUpdateEntryDTO.customerId() + " not exists into Database or has been deleted");
        }

    }
}

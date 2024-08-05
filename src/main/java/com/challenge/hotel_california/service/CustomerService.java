package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.*;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.exceptions.CustomerNotFoundException;
import com.challenge.hotel_california.exceptions.RoomNotAvailableException;
import com.challenge.hotel_california.model.Booking;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.repository.CustomerRepository;
import com.challenge.hotel_california.validatorRefactor.IValidatorCustomers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private List<IValidatorCustomers> verifyValidators;

    public Page<CustomerOutputGetListDTO> lisAllCustomers(Pageable pageable) {
        Page<Customer> findCustomers = customerRepository.findAll(pageable);
        verifyCustomerAndThrowException(findCustomers);
        return findCustomers.map(CustomerOutputGetListDTO::new);
    }

    public Customer addCustomer(CustomerEntryDTO customerEntryDTO) {
        verifyValidators.forEach(v -> v.verifyCustomersValidators(customerEntryDTO));

        return customerRepository.save(new Customer(customerEntryDTO));
    }

    public CustomerGetByIdDTO getDetailsOfASpecificCustomer(Long id) {
        Customer customer = customerRepository.getReferenceById(id);
        verifyCustomerByIdAndThrowException(id);
        return new CustomerGetByIdDTO(customer);
    }

    public CustomerOutputDTO updateAnExistingCustomer(CustomerUpdateEntryDTO customerUpdateEntryDTO, Long id) {
        Customer customer = customerRepository.getReferenceById(id);
        verifyCustomerByIdAndThrowException(id);
        if (!customer.getId().equals(customerUpdateEntryDTO.id())) {
            throw new CustomerNotFoundException("Customer " + id + " not the same of ID: " + customerUpdateEntryDTO.id());
        }
        verifyStatusRoomAndThrowException(customer);

        customer.updateCustomer(customerUpdateEntryDTO);
        return new CustomerOutputDTO(customerRepository.save(customer));
    }

    public Map<String, String> deleteACustomer(Long id) {
        Customer customer = customerRepository.getReferenceById(id);
        verifyCustomerByIdAndThrowException(id);
        verifyStatusRoomAndThrowException(customer);

        customer.deleteCustomer();

        Map<String, String> messageDelete = new HashMap<>();
        messageDelete.put("message: ", "Customer " + customer.getName() + " deleted succesfully");
        return messageDelete;
    }

    public Page<CustomerOutputGetActivatedListDTO> listAllActivatedCustomers(Pageable pageable) {
        Page<Customer> activesCustomer = customerRepository.findByIsDeletedIsFalse(pageable);
        verifyCustomerAndThrowException(activesCustomer);
        return activesCustomer.map(CustomerOutputGetActivatedListDTO::new);
    }

    private void verifyStatusRoomAndThrowException(Customer customer) {
        List<Booking> bookings = customer.getBookings();
        List<Booking> statusRoom = bookings.stream()
                .filter(s -> s.getStatus().equals(BookingStatus.CONFIRMED) || s.getStatus().equals(BookingStatus.CHECKED_IN))
                .collect(Collectors.toList());
        if (!statusRoom.isEmpty()) {
            throw new RoomNotAvailableException("Customer cannot be updated due to active bookings with unavailable room status.");
        }
    }

    private void verifyCustomerByIdAndThrowException(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer " + id + " not found!");
        }
    }

    private void verifyCustomerAndThrowException(Page customer) {
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer  not found!");
        }
    }
}

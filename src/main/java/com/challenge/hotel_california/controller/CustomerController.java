package com.challenge.hotel_california.controller;

import com.challenge.hotel_california.DTOs.*;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.service.CustomerService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerOutputGetListDTO>> listAllCustomers(@PageableDefault(size = 5, sort = {"name"}) Pageable pageable) {
        return ResponseEntity.ok(customerService.lisAllCustomers(pageable));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CustomerOutputDTO> addCustomer(@Valid @RequestBody CustomerEntryDTO customerEntryDTO, UriComponentsBuilder uriComponentsBuilder) {
        Customer customer = customerService.addCustomer(customerEntryDTO);

        URI uri = uriComponentsBuilder.path("/customers/{id}")
                .buildAndExpand(customer.getId()).toUri();
        return ResponseEntity.created(uri).body(new CustomerOutputDTO(customer));

    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerGetByIdDTO> getDetailsOfASpecificCustomer(@PathVariable Long id) {
        return ResponseEntity.ok().body(customerService.getDetailsOfASpecificCustomer(id));
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<CustomerOutputDTO> updateAnExistingCustomer(@Valid @RequestBody CustomerUpdateEntryDTO customerUpdateEntryDTO,
                                                                      @PathVariable Long id) {
        return ResponseEntity.ok().body(customerService.updateAnExistingCustomer(customerUpdateEntryDTO, id));

    }

    @DeleteMapping("/{id}")
    @Transactional
    public Map<String, String> deleteACustomer(@PathVariable Long id) {
        return customerService.deleteACustomer(id);
    }
    @GetMapping("/actives")
    public ResponseEntity<Page<CustomerOutputGetActivatedListDTO>> listAllActivatedCustomers(@PageableDefault(size = 5, sort = {"name"}) Pageable pageable) {
        return ResponseEntity.ok(customerService.listAllActivatedCustomers(pageable));
    }
}

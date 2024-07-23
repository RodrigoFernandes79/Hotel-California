package com.challenge.hotel_california.controller;

import com.challenge.hotel_california.DTOs.CustomerOutputGetListDTO;
import com.challenge.hotel_california.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerOutputGetListDTO>> listAllCustomers(@PageableDefault(size = 5, sort = {"name"}) Pageable pageable) {
        return ResponseEntity.ok(customerService.lisAllCustomers(pageable));
    }
}

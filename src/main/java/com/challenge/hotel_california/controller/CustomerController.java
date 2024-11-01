package com.challenge.hotel_california.controller;

import com.challenge.hotel_california.DTOs.*;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Customer", description = "Endpoints for Managing Customer")
@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Operation(
            summary = "Find all Customers paginated List",
            description = "Get a list of all the Customers created with pagination feature",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomerOutputGetListDTO.class)))),
                    @ApiResponse(description = "No Content", responseCode = "204",
                            content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500",
                            content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<Page<CustomerOutputGetListDTO>> listAllCustomers(@PageableDefault(size = 5, sort = {"name"}) Pageable pageable) {
        return ResponseEntity.ok(customerService.lisAllCustomers(pageable));
    }

    @Operation(
            summary = "Add a new Customer",
            description = "Create a new Customer",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = CustomerOutputDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500",
                            content = @Content)
            }
    )
    @PostMapping
    @Transactional
    public ResponseEntity<CustomerOutputDTO> addCustomer(@Valid @RequestBody CustomerEntryDTO customerEntryDTO, UriComponentsBuilder uriComponentsBuilder) {
        Customer customer = customerService.addCustomer(customerEntryDTO);

        URI uri = uriComponentsBuilder.path("/customers/{id}")
                .buildAndExpand(customer.getId()).toUri();
        return ResponseEntity.created(uri).body(new CustomerOutputDTO(customer));

    }

    @Operation(
            summary = "Get details of a specific Customer",
            description = "Retrieve the details of a specific Customer by Id",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CustomerGetByIdDTO.class))
                    ),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500",
                            content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CustomerGetByIdDTO> getDetailsOfASpecificCustomer(@PathVariable Long id) {
        return ResponseEntity.ok().body(customerService.getDetailsOfASpecificCustomer(id));
    }

    @Operation(
            summary = "Update an existing Customer",
            description = "Update the details of an existing Customer by Id",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CustomerOutputDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500",
                            content = @Content)
            }
    )
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<CustomerOutputDTO> updateAnExistingCustomer(@Valid @RequestBody CustomerUpdateEntryDTO customerUpdateEntryDTO,
                                                                      @PathVariable Long id) {
        return ResponseEntity.ok().body(customerService.updateAnExistingCustomer(customerUpdateEntryDTO, id));

    }

    @Operation(summary = "Delete a Customer", description = "Delete a Customer by Id",
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204",
                            content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401",
                            content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500",
                            content = @Content),
            }
    )
    @DeleteMapping("/{id}")
    @Transactional
    public Map<String, String> deleteACustomer(@PathVariable Long id) {
        return customerService.deleteACustomer(id);
    }

    @Operation(
            summary = "Find all activated Customers",
            description = "Get a list of only all actives Customers with pagination feature",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content =
                    @Content(array = @ArraySchema(schema = @Schema(implementation = CustomerOutputGetActivatedListDTO.class)))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204",
                            content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404",
                            content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500",
                            content = @Content)
            }
    )
    @GetMapping("/actives")
    public ResponseEntity<Page<CustomerOutputGetActivatedListDTO>> listAllActivatedCustomers(@PageableDefault(size = 5, sort = {"name"}) Pageable pageable) {
        return ResponseEntity.ok(customerService.listAllActivatedCustomers(pageable));
    }
}

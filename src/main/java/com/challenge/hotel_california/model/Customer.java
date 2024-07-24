package com.challenge.hotel_california.model;

import com.challenge.hotel_california.DTOs.CustomerEntryDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    @JsonManagedReference
    @OneToMany(mappedBy = "customerName", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    public Customer(CustomerEntryDTO customerEntryDTO) {
        this.name = customerEntryDTO.name();
        this.email = customerEntryDTO.email();
        this.phone = customerEntryDTO.phone();
    }
}

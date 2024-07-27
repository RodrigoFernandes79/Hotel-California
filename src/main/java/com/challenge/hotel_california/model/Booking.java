package com.challenge.hotel_california.model;

import com.challenge.hotel_california.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customerName;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime checkInDate;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime checkOutDate;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    private BigDecimal totalPrice;


    public Booking(Customer customer, LocalDateTime checkInDate, Room room, BigDecimal price) {
        this.customerName = customer;
        this.checkInDate = checkInDate;
        this.status = BookingStatus.CONFIRMED;
        this.room = room;
        this.totalPrice = price;
    }
}

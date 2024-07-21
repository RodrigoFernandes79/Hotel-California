package com.challenge.hotel_california.model;

import com.challenge.hotel_california.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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
    @DateTimeFormat(pattern = "dd/MM/YYYY hh:mm")
    private LocalDate checkInDate;
    @DateTimeFormat(pattern = "dd/MM/YYYY hh:mm")
    private LocalDate checkOutDate;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;


}

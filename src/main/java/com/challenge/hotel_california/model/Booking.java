package com.challenge.hotel_california.model;

import com.challenge.hotel_california.DTOs.BookingUpdateEntryDTO;
import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.enums.RoomStatus;
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
    private int daily;
    private BigDecimal totalPrice;


    public Booking(Customer customer, LocalDateTime checkInDate, Room room, BigDecimal price) {
        this.customerName = customer;
        this.checkInDate = checkInDate;
        this.status = BookingStatus.CONFIRMED;
        this.room = room;
        this.daily = 1;
        this.totalPrice = price;
    }

    public void updateBooking(Customer customerFound, BookingUpdateEntryDTO bookingUpdateEntryDTO, Room roomFound) {
        if (bookingUpdateEntryDTO.customerId() != null) {
            this.customerName = customerFound;
        }
        if (bookingUpdateEntryDTO.checkInDate() != null) {
            this.checkInDate = bookingUpdateEntryDTO.checkInDate().withHour(14);
        }
        if (bookingUpdateEntryDTO.status() != null) {
            this.status = bookingUpdateEntryDTO.status();
            if (this.status.equals(BookingStatus.COMPLETED) || this.status.equals(BookingStatus.CANCELLED)) {
                room.setStatus(RoomStatus.AVAILABLE);
            }
            if (this.status.equals(BookingStatus.CONFIRMED) || this.status.equals(BookingStatus.CHECKED_IN)) {
                room.setStatus(RoomStatus.BOOKED);
            }
        }
        if (bookingUpdateEntryDTO.roomId() != null && !this.room.getId().equals(room.getId())) {
            this.room = roomFound;
        }
    }
}

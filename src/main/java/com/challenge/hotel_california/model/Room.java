package com.challenge.hotel_california.model;

import com.challenge.hotel_california.DTOs.RoomEntryDTO;
import com.challenge.hotel_california.DTOs.RoomEntryUpdateDTO;
import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.enums.RoomType;
import com.challenge.hotel_california.exceptions.RoomNotAvailableException;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private RoomType type;
    @Enumerated(EnumType.STRING)
    private RoomStatus status;
    @JsonManagedReference
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    public Room(RoomEntryDTO roomEntryDTO) {
        this.number = roomEntryDTO.number();
        this.price = roomEntryDTO.price();
        this.type = roomEntryDTO.type();
        this.status = roomEntryDTO.status();
    }

    public void updateRoom(RoomEntryUpdateDTO roomEntryUpdateDTO) {
        if (this.number != null) {
            this.number = roomEntryUpdateDTO.number();
        }
        if (this.type != null) {
            this.type = roomEntryUpdateDTO.type();
        }
        if (this.price != null) {
            this.price = roomEntryUpdateDTO.price();
        }
        if (!this.status.equals(RoomStatus.AVAILABLE)) {
            throw new RoomNotAvailableException("Room cannot change status because it is not available.");
        }
    }
}


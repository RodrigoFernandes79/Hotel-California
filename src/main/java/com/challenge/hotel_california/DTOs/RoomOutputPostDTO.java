package com.challenge.hotel_california.DTOs;

import com.challenge.hotel_california.enums.RoomStatus;
import com.challenge.hotel_california.enums.RoomType;
import com.challenge.hotel_california.model.Room;

import java.math.BigDecimal;

public record RoomOutputPostDTO(
        Long id,
        String number,
        BigDecimal price,
        RoomType type,
        RoomStatus status
) {
    public RoomOutputPostDTO(Room room) {
        this(room.getId(), room.getNumber(), room.getPrice(), room.getType(), room.getStatus());
    }
}

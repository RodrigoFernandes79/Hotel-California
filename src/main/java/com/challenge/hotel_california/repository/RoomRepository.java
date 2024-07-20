package com.challenge.hotel_california.repository;

import com.challenge.hotel_california.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}

package com.challenge.hotel_california.repository;

import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.room.id = :id AND b.status NOT IN :status")
    List<Booking> getBookingsById(Long id, List<BookingStatus> status);

}

package com.challenge.hotel_california.repository;

import com.challenge.hotel_california.enums.BookingStatus;
import com.challenge.hotel_california.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.room.id = :id AND b.status NOT IN :status")
    List<Booking> getBookingsById(Long id, List<BookingStatus> status);

    @Query("SELECT b FROM Booking b WHERE LOWER(b.customerName.name) LIKE LOWER(CONCAT('%', :customerName ,'%'))")
    Page<Booking> findAllByCustomerNameById(Pageable pageable, @Param("customerName") String customerName);
}

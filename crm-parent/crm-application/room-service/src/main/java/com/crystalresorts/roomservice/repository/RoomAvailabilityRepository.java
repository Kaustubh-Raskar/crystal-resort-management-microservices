package com.crystalresorts.roomservice.repository;

import com.crystalresorts.roomservice.entity.RoomAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailabilityEntity, Long> {

    List<RoomAvailabilityEntity> findByRoomId(Long roomId);

    Optional<RoomAvailabilityEntity> findByRoomIdAndAvailableDate(Long roomId, LocalDate date);

    List<RoomAvailabilityEntity> findByRoomIdAndAvailableDateBetween(
            Long roomId, LocalDate startDate, LocalDate endDate);

    List<RoomAvailabilityEntity> findByRoomIdAndIsAvailable(Long roomId, Boolean isAvailable);

    List<RoomAvailabilityEntity> findByRoomIdInAndAvailableDateBetween(
            List<Long> roomIds, LocalDate startDate, LocalDate endDate);

    @Query("SELECT ra FROM RoomAvailabilityEntity ra WHERE ra.roomId = :roomId " +
           "AND ra.availableDate >= :startDate AND ra.availableDate <= :endDate " +
           "AND ra.isAvailable = true")
    List<RoomAvailabilityEntity> findAvailableDates(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    void deleteByRoomId(Long roomId);
}

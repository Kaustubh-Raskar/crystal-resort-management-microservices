package com.crystalresorts.roomservice.repository;

import com.crystalresorts.roomservice.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    List<RoomEntity> findByResortId(Long resortId);

    Optional<RoomEntity> findByRoomNumberAndResortId(String roomNumber, Long resortId);

    List<RoomEntity> findByStatus(String status);

    List<RoomEntity> findByResortIdAndStatus(Long resortId, String status);

    @Query("SELECT r FROM RoomEntity r WHERE r.resortId = :resortId AND r.status = 'AVAILABLE'")
    List<RoomEntity> findAvailableRoomsByResort(@Param("resortId") Long resortId);
}

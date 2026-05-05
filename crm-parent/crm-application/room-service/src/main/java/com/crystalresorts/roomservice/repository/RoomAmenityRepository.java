package com.crystalresorts.roomservice.repository;

import com.crystalresorts.roomservice.entity.RoomAmenityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomAmenityRepository extends JpaRepository<RoomAmenityEntity, Long> {

    List<RoomAmenityEntity> findByRoomId(Long roomId);

    void deleteByRoomId(Long roomId);

    boolean existsByRoomIdAndAmenityName(Long roomId, String amenityName);
}

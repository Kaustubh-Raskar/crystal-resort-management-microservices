package com.crystalresorts.roomservice.service;

import com.crystalresorts.roomservice.dto.RoomRequest;
import com.crystalresorts.roomservice.dto.RoomResponse;
import com.crystalresorts.roomservice.dto.RoomWithResortResponse;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    RoomResponse createRoom(RoomRequest request);

    RoomResponse getRoomById(Long id);

    List<RoomResponse> getAllRooms();

    List<RoomResponse> getRoomsByResortId(Long resortId);

    List<RoomResponse> getAvailableRoomsByResort(Long resortId);

    RoomResponse updateRoom(Long id, RoomRequest request);

    void deleteRoom(Long id);

    List<RoomResponse> getRoomsByStatus(String status);

    List<RoomResponse> getRoomsByIds(List<Long> ids);

    /**
     * BFF endpoint: Get available rooms for a resort within a date range,
     * enriched with resort information.
     */
    List<RoomWithResortResponse> getAvailableRoomsForResortWithinDateRange(
            Long resortId, LocalDate startDate, LocalDate endDate);
}

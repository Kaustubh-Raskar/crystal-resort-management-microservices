package com.crystalresorts.roomservice.service;

import com.crystalresorts.roomservice.dto.RoomRequest;
import com.crystalresorts.roomservice.dto.RoomResponse;

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
}

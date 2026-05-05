package com.crystalresorts.roomservice.service;

import com.crystalresorts.roomservice.dto.RoomAmenityRequest;
import com.crystalresorts.roomservice.dto.RoomAmenityResponse;

import java.util.List;

public interface RoomAmenityService {

    RoomAmenityResponse addAmenity(RoomAmenityRequest request);

    RoomAmenityResponse getAmenityById(Long id);

    List<RoomAmenityResponse> getAmenitiesByRoomId(Long roomId);

    RoomAmenityResponse updateAmenity(Long id, RoomAmenityRequest request);

    void deleteAmenity(Long id);

    void deleteAmenitiesByRoomId(Long roomId);
}

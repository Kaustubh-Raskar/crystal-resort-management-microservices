package com.crystalresorts.roomservice.service;

import com.crystalresorts.roomservice.dto.RoomAvailabilityRequest;
import com.crystalresorts.roomservice.dto.RoomAvailabilityResponse;

import java.time.LocalDate;
import java.util.List;

public interface RoomAvailabilityService {

    RoomAvailabilityResponse addAvailability(RoomAvailabilityRequest request);

    RoomAvailabilityResponse getAvailabilityById(Long id);

    RoomAvailabilityResponse getAvailabilityByRoomAndDate(Long roomId, LocalDate date);

    List<RoomAvailabilityResponse> getAvailabilitiesByRoomId(Long roomId);

    List<RoomAvailabilityResponse> getAvailableDates(Long roomId, LocalDate startDate, LocalDate endDate);

    List<RoomAvailabilityResponse> getAvailabilitiesByRoomAndDateRange(
            Long roomId, LocalDate startDate, LocalDate endDate);

    RoomAvailabilityResponse updateAvailability(Long id, RoomAvailabilityRequest request);

    void deleteAvailability(Long id);

    void deleteAvailabilitiesByRoomId(Long roomId);

    void markAsBooked(Long roomId, LocalDate date, Long bookingId);

    void markAsAvailable(Long roomId, LocalDate date);
}

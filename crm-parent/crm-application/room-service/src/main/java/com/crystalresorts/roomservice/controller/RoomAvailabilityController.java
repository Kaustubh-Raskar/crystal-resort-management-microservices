package com.crystalresorts.roomservice.controller;

import com.crystalresorts.roomservice.dto.RoomAvailabilityRequest;
import com.crystalresorts.roomservice.dto.RoomAvailabilityResponse;
import com.crystalresorts.roomservice.service.RoomAvailabilityService;
import com.crystalresorts.roomservice.service.RoomService;
import com.crystalresorts.roomservice.dto.RoomResponse;
import com.crystalresorts.roomservice.dto.RoomWithResortResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/room-availability")
@Slf4j
public class RoomAvailabilityController {

    @Autowired
    private RoomAvailabilityService availabilityService;

    @Autowired
    private RoomService roomService;

    /**
     * Add availability record for a room (ADMIN only)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomAvailabilityResponse> addAvailability(@RequestBody RoomAvailabilityRequest request) {
        log.info("Adding availability for room: {} on date: {}", request.getRoomId(), request.getAvailableDate());
        RoomAvailabilityResponse response = availabilityService.addAvailability(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get availability by ID (Any authenticated user)
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RoomAvailabilityResponse> getAvailabilityById(@PathVariable Long id) {
        log.info("Fetching availability with ID: {}", id);
        RoomAvailabilityResponse response = availabilityService.getAvailabilityById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get availability for a specific room and date (Any authenticated user)
     */
    @GetMapping("/room/{roomId}/date/{date}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RoomAvailabilityResponse> getAvailabilityByRoomAndDate(
            @PathVariable Long roomId,
            @PathVariable LocalDate date) {
        log.info("Fetching availability for room: {} on date: {}", roomId, date);
        RoomAvailabilityResponse response = availabilityService.getAvailabilityByRoomAndDate(roomId, date);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all availability records for a room (Any authenticated user)
     */
    @GetMapping("/room/{roomId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoomAvailabilityResponse>> getAvailabilitiesByRoom(@PathVariable Long roomId) {
        log.info("Fetching all availabilities for room: {}", roomId);
        List<RoomAvailabilityResponse> response = availabilityService.getAvailabilitiesByRoomId(roomId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get available dates for a room within a date range (Any authenticated user)
     */
    @GetMapping("/room/{roomId}/available")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoomAvailabilityResponse>> getAvailableDates(
            @PathVariable Long roomId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("Fetching available dates for room: {} between {} and {}", roomId, startDate, endDate);
        List<RoomAvailabilityResponse> response = availabilityService.getAvailableDates(roomId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    /**
     * Get availability records for a room within a date range (Any authenticated user)
     */
    @GetMapping("/room/{roomId}/date-range")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoomAvailabilityResponse>> getAvailabilitiesByDateRange(
            @PathVariable Long roomId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("Fetching availabilities for room: {} between {} and {}", roomId, startDate, endDate);
        List<RoomAvailabilityResponse> response = availabilityService.getAvailabilitiesByRoomAndDateRange(roomId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    /**
     * Update availability (ADMIN only)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomAvailabilityResponse> updateAvailability(
            @PathVariable Long id,
            @RequestBody RoomAvailabilityRequest request) {
        log.info("Updating availability with ID: {}", id);
        RoomAvailabilityResponse response = availabilityService.updateAvailability(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete availability (ADMIN only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        log.info("Deleting availability with ID: {}", id);
        availabilityService.deleteAvailability(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Mark a room as booked for a specific date (ADMIN only)
     * Used by booking service to reserve a room
     */
    @PostMapping("/room/{roomId}/date/{date}/book/{bookingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> markAsBooked(
            @PathVariable Long roomId,
            @PathVariable LocalDate date,
            @PathVariable Long bookingId) {
        log.info("Marking room: {} as booked on date: {} with booking ID: {}", roomId, date, bookingId);
        availabilityService.markAsBooked(roomId, date, bookingId);
        return ResponseEntity.ok().build();
    }

    /**
     * Mark a room as available for a specific date (ADMIN only)
     * Used to cancel a booking and release the room
     */
    @PostMapping("/room/{roomId}/date/{date}/release")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> markAsAvailable(
            @PathVariable Long roomId,
            @PathVariable LocalDate date) {
        log.info("Marking room: {} as available on date: {}", roomId, date);
        availabilityService.markAsAvailable(roomId, date);
        return ResponseEntity.ok().build();
    }

    /**
     * Given a list of roomIds and a date range, return the Room details
     * for rooms that are fully available for every date in the range.
     */
    @GetMapping("/rooms")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoomResponse>> getAvailableRoomsForRoomIds(
            @RequestParam List<Long> roomIds,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        log.info("Checking availability for rooms {} between {} and {}", roomIds, startDate, endDate);

        List<Long> availableRoomIds = availabilityService.getRoomsAvailableForRange(roomIds, startDate, endDate);

        List<RoomResponse> rooms = roomService.getRoomsByIds(availableRoomIds);
        return ResponseEntity.ok(rooms);
    }

    /**
     * BFF endpoint: Get available rooms for a resort within a date range,
     * enriched with resort information (name, city, address, rating).
     * Single call returns both room and resort details.
     */
    @GetMapping("/resort/{resortId}/available")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoomWithResortResponse>> getAvailableRoomsForResort(
            @PathVariable Long resortId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        log.info("Fetching available rooms for resort {} with details between {} and {}",
                resortId, startDate, endDate);

        List<RoomWithResortResponse> rooms = roomService.getAvailableRoomsForResortWithinDateRange(
                resortId, startDate, endDate);

        return ResponseEntity.ok(rooms);
    }
}

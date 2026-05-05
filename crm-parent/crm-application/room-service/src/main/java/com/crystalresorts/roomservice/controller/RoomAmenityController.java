package com.crystalresorts.roomservice.controller;

import com.crystalresorts.roomservice.dto.RoomAmenityRequest;
import com.crystalresorts.roomservice.dto.RoomAmenityResponse;
import com.crystalresorts.roomservice.service.RoomAmenityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-amenities")
@Slf4j
public class RoomAmenityController {

    @Autowired
    private RoomAmenityService amenityService;

    /**
     * Add a new amenity to a room (ADMIN only)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomAmenityResponse> addAmenity(@RequestBody RoomAmenityRequest request) {
        log.info("Adding amenity '{}' to room: {}", request.getAmenityName(), request.getRoomId());
        RoomAmenityResponse response = amenityService.addAmenity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get amenity by ID (Any authenticated user)
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RoomAmenityResponse> getAmenityById(@PathVariable Long id) {
        log.info("Fetching amenity with ID: {}", id);
        RoomAmenityResponse response = amenityService.getAmenityById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all amenities for a room (Any authenticated user)
     */
    @GetMapping("/room/{roomId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoomAmenityResponse>> getAmenitiesByRoom(@PathVariable Long roomId) {
        log.info("Fetching amenities for room: {}", roomId);
        List<RoomAmenityResponse> response = amenityService.getAmenitiesByRoomId(roomId);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an amenity (ADMIN only)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomAmenityResponse> updateAmenity(
            @PathVariable Long id,
            @RequestBody RoomAmenityRequest request) {
        log.info("Updating amenity with ID: {}", id);
        RoomAmenityResponse response = amenityService.updateAmenity(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an amenity (ADMIN only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        log.info("Deleting amenity with ID: {}", id);
        amenityService.deleteAmenity(id);
        return ResponseEntity.noContent().build();
    }
}

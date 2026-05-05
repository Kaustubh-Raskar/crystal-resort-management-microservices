package com.crystalresorts.roomservice.controller;

import com.crystalresorts.roomservice.dto.RoomRequest;
import com.crystalresorts.roomservice.dto.RoomResponse;
import com.crystalresorts.roomservice.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Slf4j
public class RoomController {

    @Autowired
    private RoomService roomService;

    /**
     * Create a new room (ADMIN only)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest request) {
        log.info("Creating room for resort: {}", request.getResortId());
        RoomResponse response = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get room by ID (Any authenticated user)
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        log.info("Fetching room with ID: {}", id);
        RoomResponse response = roomService.getRoomById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all rooms (Any authenticated user)
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        log.info("Fetching all rooms");
        List<RoomResponse> response = roomService.getAllRooms();
        return ResponseEntity.ok(response);
    }

    /**
     * Get rooms by resort ID (Any authenticated user)
     */
    @GetMapping("/resort/{resortId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoomResponse>> getRoomsByResort(@PathVariable Long resortId) {
        log.info("Fetching rooms for resort: {}", resortId);
        List<RoomResponse> response = roomService.getRoomsByResortId(resortId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get available rooms for a resort (Any authenticated user)
     */
    @GetMapping("/resort/{resortId}/available")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(@PathVariable Long resortId) {
        log.info("Fetching available rooms for resort: {}", resortId);
        List<RoomResponse> response = roomService.getAvailableRoomsByResort(resortId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get rooms by status (Any authenticated user)
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoomResponse>> getRoomsByStatus(@PathVariable String status) {
        log.info("Fetching rooms with status: {}", status);
        List<RoomResponse> response = roomService.getRoomsByStatus(status);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a room (ADMIN only)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @RequestBody RoomRequest request) {
        log.info("Updating room with ID: {}", id);
        RoomResponse response = roomService.updateRoom(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a room (ADMIN only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        log.info("Deleting room with ID: {}", id);
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}

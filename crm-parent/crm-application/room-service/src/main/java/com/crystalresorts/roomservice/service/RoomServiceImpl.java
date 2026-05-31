package com.crystalresorts.roomservice.service;

import com.crystalresorts.roomservice.client.ResortClient;
import com.crystalresorts.roomservice.dto.RoomRequest;
import com.crystalresorts.roomservice.dto.RoomResponse;
import com.crystalresorts.roomservice.dto.ResortResponse;
import com.crystalresorts.roomservice.dto.RoomWithResortResponse;
import com.crystalresorts.roomservice.entity.RoomEntity;
import com.crystalresorts.roomservice.mapper.RoomMapper;
import com.crystalresorts.roomservice.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {
    
    private static final Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RoomAvailabilityService roomAvailabilityService;

    @Autowired
    private ResortClient resortClient;

    @Override
    public RoomResponse createRoom(RoomRequest request) {
        
        log.info("Creating room with room number: {} for resort: {}", 
                request.getRoomNumber(), request.getResortId());

        // Check if room already exists
        roomRepository.findByRoomNumberAndResortId(request.getRoomNumber(), request.getResortId())
                .ifPresent(room -> {
                    throw new IllegalArgumentException(
                            "Room already exists with number: " + request.getRoomNumber());
                });

        RoomEntity entity = roomMapper.toEntity(request);
        RoomEntity saved = roomRepository.save(entity);

        log.info("Room created successfully with ID: {}", saved.getId());
        return roomMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Long id) {
        log.info("Fetching room with ID: {}", id);

        RoomEntity entity = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + id));

        return roomMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        log.info("Fetching all rooms");
        List<RoomEntity> entities = roomRepository.findAll();
        return roomMapper.toResponseList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByResortId(Long resortId) {
        log.info("Fetching rooms for resort: {}", resortId);
        List<RoomEntity> entities = roomRepository.findByResortId(resortId);
        return roomMapper.toResponseList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getAvailableRoomsByResort(Long resortId) {
        log.info("Fetching available rooms for resort: {}", resortId);
        List<RoomEntity> entities = roomRepository.findAvailableRoomsByResort(resortId);
        return roomMapper.toResponseList(entities);
    }

    @Override
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        log.info("Updating room with ID: {}", id);

        RoomEntity entity = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + id));

        roomMapper.updateEntityFromRequest(request, entity);
        RoomEntity updated = roomRepository.save(entity);

        log.info("Room updated successfully with ID: {}", updated.getId());
        return roomMapper.toResponse(updated);
    }

    @Override
    public void deleteRoom(Long id) {
        log.info("Deleting room with ID: {}", id);

        if (!roomRepository.existsById(id)) {
            throw new IllegalArgumentException("Room not found with ID: " + id);
        }

        roomRepository.deleteById(id);
        log.info("Room deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByStatus(String status) {
        log.info("Fetching rooms with status: {}", status);
        List<RoomEntity> entities = roomRepository.findByStatus(status);
        return roomMapper.toResponseList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByIds(List<Long> ids) {
        log.info("Fetching rooms by ids: {}", ids);
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        List<RoomEntity> entities = roomRepository.findAllById(ids);
        return roomMapper.toResponseList(entities.stream().collect(Collectors.toList()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomWithResortResponse> getAvailableRoomsForResortWithinDateRange(
            Long resortId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching available rooms for resort {} between {} and {}", resortId, startDate, endDate);

        // Step 1: Get rooms for the resort
        List<RoomEntity> rooms = roomRepository.findByResortId(resortId);
        if (rooms.isEmpty()) {
            log.warn("No rooms found for resort {}", resortId);
            return new ArrayList<>();
        }

        // Step 2: Extract room IDs and check availability
        List<Long> roomIds = rooms.stream().map(RoomEntity::getId).collect(Collectors.toList());
        List<Long> availableRoomIds = roomAvailabilityService.getRoomsAvailableForRange(
                roomIds, startDate, endDate);

        if (availableRoomIds.isEmpty()) {
            log.info("No rooms available for resort {} in date range {}-{}", resortId, startDate, endDate);
            return new ArrayList<>();
        }

        // Step 3: Fetch resort info via Feign client
        ResortResponse resort;
        try {
            resort = resortClient.getResortById(resortId);
        } catch (Exception e) {
            log.error("Failed to fetch resort details for resort {}", resortId, e);
            throw new RuntimeException("Failed to fetch resort details", e);
        }

        // Step 4: Build enriched response
        List<RoomEntity> availableRooms = rooms.stream()
                .filter(r -> availableRoomIds.contains(r.getId()))
                .collect(Collectors.toList());

        return availableRooms.stream()
                .map(room -> RoomWithResortResponse.builder()
                        .roomId(room.getId())
                        .roomNumber(room.getRoomNumber())
                        .roomType(room.getType())
                        .price(room.getBasePrice().doubleValue())
                        .capacity(room.getCapacity())
                        .status(room.getStatus())
                        .resortId(resort.getId())
                        .resortName(resort.getName())
                        .city(resort.getCity())
                        .address(resort.getAddress())
                        .rating(resort.getRating())
                        .createdAt(room.getCreatedAt())
                        .updatedAt(room.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}

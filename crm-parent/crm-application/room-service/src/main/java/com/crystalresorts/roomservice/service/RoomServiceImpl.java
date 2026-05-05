package com.crystalresorts.roomservice.service;

import com.crystalresorts.roomservice.dto.RoomRequest;
import com.crystalresorts.roomservice.dto.RoomResponse;
import com.crystalresorts.roomservice.entity.RoomEntity;
import com.crystalresorts.roomservice.mapper.RoomMapper;
import com.crystalresorts.roomservice.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

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
}

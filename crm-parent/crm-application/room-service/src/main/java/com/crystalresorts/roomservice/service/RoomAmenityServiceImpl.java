package com.crystalresorts.roomservice.service;

import com.crystalresorts.roomservice.dto.RoomAmenityRequest;
import com.crystalresorts.roomservice.dto.RoomAmenityResponse;
import com.crystalresorts.roomservice.entity.RoomAmenityEntity;
import com.crystalresorts.roomservice.mapper.RoomAmenityMapper;
import com.crystalresorts.roomservice.repository.RoomAmenityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class RoomAmenityServiceImpl implements RoomAmenityService {

    @Autowired
    private RoomAmenityRepository amenityRepository;

    @Autowired
    private RoomAmenityMapper amenityMapper;

    @Override
    public RoomAmenityResponse addAmenity(RoomAmenityRequest request) {
        log.info("Adding amenity '{}' to room: {}", request.getAmenityName(), request.getRoomId());

        // Check if amenity already exists
        if (amenityRepository.existsByRoomIdAndAmenityName(request.getRoomId(), request.getAmenityName())) {
            throw new IllegalArgumentException("Amenity already exists for this room");
        }

        RoomAmenityEntity entity = amenityMapper.toEntity(request);
        RoomAmenityEntity saved = amenityRepository.save(entity);

        log.info("Amenity added successfully with ID: {}", saved.getId());
        return amenityMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomAmenityResponse getAmenityById(Long id) {
        log.info("Fetching amenity with ID: {}", id);

        RoomAmenityEntity entity = amenityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Amenity not found with ID: " + id));

        return amenityMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomAmenityResponse> getAmenitiesByRoomId(Long roomId) {
        log.info("Fetching amenities for room: {}", roomId);

        List<RoomAmenityEntity> entities = amenityRepository.findByRoomId(roomId);
        return amenityMapper.toResponseList(entities);
    }

    @Override
    public RoomAmenityResponse updateAmenity(Long id, RoomAmenityRequest request) {
        log.info("Updating amenity with ID: {}", id);

        RoomAmenityEntity entity = amenityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Amenity not found with ID: " + id));

        if (request.getAmenityName() != null) {
            entity.setAmenityName(request.getAmenityName());
        }

        RoomAmenityEntity updated = amenityRepository.save(entity);

        log.info("Amenity updated successfully with ID: {}", updated.getId());
        return amenityMapper.toResponse(updated);
    }

    @Override
    public void deleteAmenity(Long id) {
        log.info("Deleting amenity with ID: {}", id);

        if (!amenityRepository.existsById(id)) {
            throw new IllegalArgumentException("Amenity not found with ID: " + id);
        }

        amenityRepository.deleteById(id);
        log.info("Amenity deleted successfully with ID: {}", id);
    }

    @Override
    public void deleteAmenitiesByRoomId(Long roomId) {
        log.info("Deleting all amenities for room: {}", roomId);
        amenityRepository.deleteByRoomId(roomId);
        log.info("All amenities deleted for room: {}", roomId);
    }
}

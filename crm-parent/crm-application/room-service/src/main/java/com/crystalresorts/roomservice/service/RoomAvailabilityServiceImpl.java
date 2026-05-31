package com.crystalresorts.roomservice.service;

import com.crystalresorts.roomservice.dto.RoomAvailabilityRequest;
import com.crystalresorts.roomservice.dto.RoomAvailabilityResponse;
import com.crystalresorts.roomservice.entity.RoomAvailabilityEntity;
import com.crystalresorts.roomservice.mapper.RoomAvailabilityMapper;
import com.crystalresorts.roomservice.repository.RoomAvailabilityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
@Slf4j
public class RoomAvailabilityServiceImpl implements RoomAvailabilityService {

    @Autowired
    private RoomAvailabilityRepository availabilityRepository;

    @Autowired
    private RoomAvailabilityMapper availabilityMapper;

    @Override
    public RoomAvailabilityResponse addAvailability(RoomAvailabilityRequest request) {
        log.info("Adding availability for room: {} on date: {}", 
                request.getRoomId(), request.getAvailableDate());

        // Check if availability already exists
        availabilityRepository.findByRoomIdAndAvailableDate(request.getRoomId(), request.getAvailableDate())
                .ifPresent(av -> {
                    throw new IllegalArgumentException("Availability record already exists for this date");
                });

        RoomAvailabilityEntity entity = availabilityMapper.toEntity(request);
        RoomAvailabilityEntity saved = availabilityRepository.save(entity);

        log.info("Availability added successfully with ID: {}", saved.getId());
        return availabilityMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomAvailabilityResponse getAvailabilityById(Long id) {
        log.info("Fetching availability with ID: {}", id);

        RoomAvailabilityEntity entity = availabilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Availability not found with ID: " + id));

        return availabilityMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomAvailabilityResponse getAvailabilityByRoomAndDate(Long roomId, LocalDate date) {
        log.info("Fetching availability for room: {} on date: {}", roomId, date);

        RoomAvailabilityEntity entity = availabilityRepository.findByRoomIdAndAvailableDate(roomId, date)
                .orElseThrow(() -> new IllegalArgumentException("No availability record found for this date"));

        return availabilityMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomAvailabilityResponse> getAvailabilitiesByRoomId(Long roomId) {
        log.info("Fetching all availabilities for room: {}", roomId);

        List<RoomAvailabilityEntity> entities = availabilityRepository.findByRoomId(roomId);
        return availabilityMapper.toResponseList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomAvailabilityResponse> getAvailableDates(Long roomId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching available dates for room: {} between {} and {}", roomId, startDate, endDate);

        List<RoomAvailabilityEntity> entities = availabilityRepository.findAvailableDates(roomId, startDate, endDate);
        return availabilityMapper.toResponseList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomAvailabilityResponse> getAvailabilitiesByRoomAndDateRange(
            Long roomId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching availabilities for room: {} between {} and {}", roomId, startDate, endDate);

        List<RoomAvailabilityEntity> entities = availabilityRepository.findByRoomIdAndAvailableDateBetween(roomId, startDate, endDate);
        return availabilityMapper.toResponseList(entities);
    }

    @Override
    public RoomAvailabilityResponse updateAvailability(Long id, RoomAvailabilityRequest request) {
        log.info("Updating availability with ID: {}", id);

        RoomAvailabilityEntity entity = availabilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Availability not found with ID: " + id));

        if (request.getIsAvailable() != null) {
            entity.setIsAvailable(request.getIsAvailable());
        }
        if (request.getDynamicPrice() != null) {
            entity.setDynamicPrice(request.getDynamicPrice());
        }
        if (request.getBookingId() != null) {
            entity.setBookingId(request.getBookingId());
        }

        RoomAvailabilityEntity updated = availabilityRepository.save(entity);

        log.info("Availability updated successfully with ID: {}", updated.getId());
        return availabilityMapper.toResponse(updated);
    }

    @Override
    public void deleteAvailability(Long id) {
        log.info("Deleting availability with ID: {}", id);

        if (!availabilityRepository.existsById(id)) {
            throw new IllegalArgumentException("Availability not found with ID: " + id);
        }

        availabilityRepository.deleteById(id);
        log.info("Availability deleted successfully with ID: {}", id);
    }

    @Override
    public void deleteAvailabilitiesByRoomId(Long roomId) {
        log.info("Deleting all availabilities for room: {}", roomId);
        availabilityRepository.deleteByRoomId(roomId);
        log.info("All availabilities deleted for room: {}", roomId);
    }

    @Override
    public void markAsBooked(Long roomId, LocalDate date, Long bookingId) {
        log.info("Marking room: {} as booked on date: {}", roomId, date);

        RoomAvailabilityEntity entity = availabilityRepository.findByRoomIdAndAvailableDate(roomId, date)
                .orElseThrow(() -> new IllegalArgumentException("Availability record not found"));

        entity.setIsAvailable(false);
        entity.setBookingId(bookingId);
        availabilityRepository.save(entity);

        log.info("Room marked as booked with booking ID: {}", bookingId);
    }

    @Override
    public void markAsAvailable(Long roomId, LocalDate date) {
        log.info("Marking room: {} as available on date: {}", roomId, date);

        RoomAvailabilityEntity entity = availabilityRepository.findByRoomIdAndAvailableDate(roomId, date)
                .orElseThrow(() -> new IllegalArgumentException("Availability record not found"));

        entity.setIsAvailable(true);
        entity.setBookingId(null);
        availabilityRepository.save(entity);

        log.info("Room marked as available");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getRoomsAvailableForRange(List<Long> roomIds, LocalDate startDate, LocalDate endDate) {
        log.info("Checking availability for rooms: {} between {} and {}", roomIds, startDate, endDate);

        if (roomIds == null || roomIds.isEmpty()) {
            return List.of();
        }

        List<RoomAvailabilityEntity> entities = availabilityRepository.findByRoomIdInAndAvailableDateBetween(roomIds, startDate, endDate);

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        Map<Long, Set<LocalDate>> availableDatesByRoom = entities.stream()
                .filter(e -> Boolean.TRUE.equals(e.getIsAvailable()))
                .collect(Collectors.groupingBy(RoomAvailabilityEntity::getRoomId,
                        Collectors.mapping(RoomAvailabilityEntity::getAvailableDate, Collectors.toSet())));

        List<Long> fullyAvailable = availableDatesByRoom.entrySet().stream()
                .filter(entry -> entry.getValue().size() == days)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return fullyAvailable;
    }
}

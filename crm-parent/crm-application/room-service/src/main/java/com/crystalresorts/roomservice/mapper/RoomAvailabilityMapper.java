package com.crystalresorts.roomservice.mapper;

import com.crystalresorts.roomservice.dto.RoomAvailabilityRequest;
import com.crystalresorts.roomservice.dto.RoomAvailabilityResponse;
import com.crystalresorts.roomservice.entity.RoomAvailabilityEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomAvailabilityMapper {

    public RoomAvailabilityEntity toEntity(RoomAvailabilityRequest request) {
        if (request == null) {
            return null;
        }

        return RoomAvailabilityEntity.builder()
                .roomId(request.getRoomId())
                .availableDate(request.getAvailableDate())
                .isAvailable(request.getIsAvailable())
                .dynamicPrice(request.getDynamicPrice())
                .bookingId(request.getBookingId())
                .build();
    }

    public RoomAvailabilityResponse toResponse(RoomAvailabilityEntity entity) {
        if (entity == null) {
            return null;
        }

        return RoomAvailabilityResponse.builder()
                .id(entity.getId())
                .roomId(entity.getRoomId())
                .availableDate(entity.getAvailableDate())
                .isAvailable(entity.getIsAvailable())
                .dynamicPrice(entity.getDynamicPrice())
                .bookingId(entity.getBookingId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public List<RoomAvailabilityResponse> toResponseList(List<RoomAvailabilityEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

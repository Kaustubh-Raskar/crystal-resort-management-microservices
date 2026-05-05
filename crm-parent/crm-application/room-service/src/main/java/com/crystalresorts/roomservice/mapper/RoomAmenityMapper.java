package com.crystalresorts.roomservice.mapper;

import com.crystalresorts.roomservice.dto.RoomAmenityRequest;
import com.crystalresorts.roomservice.dto.RoomAmenityResponse;
import com.crystalresorts.roomservice.entity.RoomAmenityEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomAmenityMapper {

    public RoomAmenityEntity toEntity(RoomAmenityRequest request) {
        if (request == null) {
            return null;
        }

        return RoomAmenityEntity.builder()
                .roomId(request.getRoomId())
                .amenityName(request.getAmenityName())
                .build();
    }

    public RoomAmenityResponse toResponse(RoomAmenityEntity entity) {
        if (entity == null) {
            return null;
        }

        return RoomAmenityResponse.builder()
                .id(entity.getId())
                .roomId(entity.getRoomId())
                .amenityName(entity.getAmenityName())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public List<RoomAmenityResponse> toResponseList(List<RoomAmenityEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

package com.crystalresorts.roomservice.mapper;

import com.crystalresorts.roomservice.dto.RoomRequest;
import com.crystalresorts.roomservice.dto.RoomResponse;
import com.crystalresorts.roomservice.entity.RoomEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMapper {

    public RoomEntity toEntity(RoomRequest request) {
        if (request == null) {
            return null;
        }

        return RoomEntity.builder()
                .resortId(request.getResortId())
                .roomNumber(request.getRoomNumber())
                .floor(request.getFloor())
                .type(request.getType())
                .capacity(request.getCapacity())
                .basePrice(request.getBasePrice())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();
    }

    public RoomResponse toResponse(RoomEntity entity) {
        if (entity == null) {
            return null;
        }

        return RoomResponse.builder()
                .id(entity.getId())
                .resortId(entity.getResortId())
                .roomNumber(entity.getRoomNumber())
                .floor(entity.getFloor())
                .type(entity.getType())
                .capacity(entity.getCapacity())
                .basePrice(entity.getBasePrice())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public List<RoomResponse> toResponseList(List<RoomEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(RoomRequest request, RoomEntity entity) {
        if (request == null || entity == null) {
            return;
        }

        if (request.getRoomNumber() != null) {
            entity.setRoomNumber(request.getRoomNumber());
        }
        if (request.getFloor() != null) {
            entity.setFloor(request.getFloor());
        }
        if (request.getType() != null) {
            entity.setType(request.getType());
        }
        if (request.getCapacity() != null) {
            entity.setCapacity(request.getCapacity());
        }
        if (request.getBasePrice() != null) {
            entity.setBasePrice(request.getBasePrice());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }
    }
}

package com.crystalresorts.resortservice.mapper;

import com.crystalresorts.resortservice.dto.ResortRequest;
import com.crystalresorts.resortservice.dto.ResortResponse;
import com.crystalresorts.resortservice.entity.ResortEntity;

public class ResortMapper {

    private ResortMapper() {}

    public static ResortEntity toEntity(ResortRequest request) {
        return ResortEntity.builder()
                .name(request.getName())
                .city(request.getCity())
                .address(request.getAddress())
                .rating(request.getRating())
                .build();
    }

    public static ResortResponse toResponse(ResortEntity entity) {
        return ResortResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .city(entity.getCity())
                .address(entity.getAddress())
                .rating(entity.getRating())
                .status(entity.getStatus().name())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
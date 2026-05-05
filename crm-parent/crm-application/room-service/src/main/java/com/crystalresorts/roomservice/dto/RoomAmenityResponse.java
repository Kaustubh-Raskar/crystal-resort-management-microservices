package com.crystalresorts.roomservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAmenityResponse {

    private Long id;
    private Long roomId;
    private String amenityName;
    private LocalDateTime createdAt;
}

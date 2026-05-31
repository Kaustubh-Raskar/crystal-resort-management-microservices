package com.crystalresorts.roomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * BFF response: room details enriched with resort information.
 */
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomWithResortResponse {
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private Double price;
    private Integer capacity;
    private String status;
    
    // Resort enrichment
    private Long resortId;
    private String resortName;
    private String city;
    private String address;
    private Double rating;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

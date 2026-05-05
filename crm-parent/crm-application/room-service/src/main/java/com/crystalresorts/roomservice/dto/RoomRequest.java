package com.crystalresorts.roomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequest {

    private Long resortId;
    private String roomNumber;
    private Integer floor;
    private String type;
    private Integer capacity;
    private BigDecimal basePrice;
    private String description;
    private String status;
}

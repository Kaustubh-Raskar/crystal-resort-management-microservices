package com.crystalresorts.roomservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
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

    public Long getResortId() {
        return resortId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}

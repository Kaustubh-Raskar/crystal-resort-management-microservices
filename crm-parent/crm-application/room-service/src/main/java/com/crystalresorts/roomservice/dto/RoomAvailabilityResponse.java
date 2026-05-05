package com.crystalresorts.roomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAvailabilityResponse {

    private Long id;
    private Long roomId;
    private LocalDate availableDate;
    private Boolean isAvailable;
    private BigDecimal dynamicPrice;
    private Long bookingId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

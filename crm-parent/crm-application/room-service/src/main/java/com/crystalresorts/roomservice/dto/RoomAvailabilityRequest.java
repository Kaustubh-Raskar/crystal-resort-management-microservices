package com.crystalresorts.roomservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAvailabilityRequest {

    private Long roomId;
    private LocalDate availableDate;
    private Boolean isAvailable;
    private BigDecimal dynamicPrice;
    private Long bookingId;
}

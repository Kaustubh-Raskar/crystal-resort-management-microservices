package com.crystalresorts.resortservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
public class ResortResponse {

    private Long id;
    private String name;
    private String city;
    private String address;
    private Double rating;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

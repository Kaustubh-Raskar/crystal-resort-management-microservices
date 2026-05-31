package com.crystalresorts.roomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

package com.crystalresorts.resortservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResortRequest {

    @NotBlank(message = "Resort name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Address is required")
    private String address;

    private Double rating;
}



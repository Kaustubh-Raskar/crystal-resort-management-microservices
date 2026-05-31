package com.crystalresorts.roomservice.dto;

import java.time.LocalDateTime;

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
public class RoomWithResortResponse {
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private Double price;
    private Integer capacity;
    private String status;
    
    private Long resortId;
    private String resortName;
    private String city;
    private String address;
    private Double rating;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResortId(Long resortId) {
        this.resortId = resortId;
    }

    public void setResortName(String resortName) {
        this.resortName = resortName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

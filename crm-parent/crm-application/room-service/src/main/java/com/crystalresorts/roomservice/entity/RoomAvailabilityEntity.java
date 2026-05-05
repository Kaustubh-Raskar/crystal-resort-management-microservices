package com.crystalresorts.roomservice.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RoomAvailability", schema = "dbo", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"RoomId", "AvailableDate"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAvailabilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "RoomId", nullable = false)
    private Long roomId;

    @Column(name = "AvailableDate", nullable = false)
    private LocalDate availableDate;

    @Column(name = "IsAvailable")
    private Boolean isAvailable;

    @Column(name = "DynamicPrice", precision = 10, scale = 2)
    private BigDecimal dynamicPrice;

    @Column(name = "BookingId")
    private Long bookingId;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        if (isAvailable == null) {
            isAvailable = true;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

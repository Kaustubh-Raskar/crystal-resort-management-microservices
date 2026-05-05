package com.crystalresorts.roomservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Rooms", schema = "dbo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "ResortId", nullable = false)
    private Long resortId;

    @Column(name = "RoomNumber", nullable = false, length = 10)
    private String roomNumber;

    @Column(name = "Floor")
    private Integer floor;

    @Column(name = "Type", length = 50)
    private String type; // Single, Double, Suite, Deluxe, etc.

    @Column(name = "Capacity")
    private Integer capacity;

    @Column(name = "BasePrice", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "Status", length = 20)
    private String status; // AVAILABLE, MAINTENANCE, OCCUPIED, CLOSED

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = null;
        if (status == null) {
            status = "AVAILABLE";
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

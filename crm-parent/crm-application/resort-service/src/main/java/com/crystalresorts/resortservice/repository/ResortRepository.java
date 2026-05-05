package com.crystalresorts.resortservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crystalresorts.resortservice.entity.ResortEntity;

public interface ResortRepository extends JpaRepository<ResortEntity, Long> {
}
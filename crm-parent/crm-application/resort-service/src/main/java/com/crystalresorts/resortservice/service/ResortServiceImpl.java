package com.crystalresorts.resortservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.crystalresorts.resortservice.dto.ResortRequest;
import com.crystalresorts.resortservice.dto.ResortResponse;
import com.crystalresorts.resortservice.entity.ResortEntity;
import com.crystalresorts.resortservice.exception.ResortNotFoundException;
import com.crystalresorts.resortservice.mapper.ResortMapper;
import com.crystalresorts.resortservice.repository.ResortRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResortServiceImpl implements ResortService {

    private final ResortRepository resortRepository;

    @Override
    public ResortResponse createResort(ResortRequest request) {
    
        if (request.getRating() != null &&
            (request.getRating() < 0 || request.getRating() > 5)) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
    
        ResortEntity resort = ResortMapper.toEntity(request);
        return ResortMapper.toResponse(resortRepository.save(resort));
    }
    

    @Override
    public List<ResortResponse> getAllResorts() {
        return resortRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ResortResponse getResortById(Long id) {
        ResortEntity resort = resortRepository.findById(id)
                .orElseThrow(() -> new ResortNotFoundException(id));
        return ResortMapper.toResponse(resort);
    }

    @Override
    public void deleteResort(Long id) {
        resortRepository.deleteById(id);
    }

    private ResortResponse mapToResponse(ResortEntity resort) {
        return ResortResponse.builder()
                .id(resort.getId())
                .name(resort.getName())
                .city(resort.getCity())
                .address(resort.getAddress())
                .rating(resort.getRating())
                .status(resort.getStatus().name())
                .build();
    }
}

package com.crystalresorts.resortservice.service;

import java.util.List;

import com.crystalresorts.resortservice.dto.ResortRequest;
import com.crystalresorts.resortservice.dto.ResortResponse;

public interface ResortService {

    ResortResponse createResort(ResortRequest request);

    List<ResortResponse> getAllResorts();

    ResortResponse getResortById(Long id);

    void deleteResort(Long id);
}
package com.crystalresorts.roomservice.client;

import com.crystalresorts.roomservice.dto.ResortResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "resort-service")
public interface ResortClient {

    @GetMapping("/api/resorts/{id}")
    ResortResponse getResortById(@PathVariable("id") Long id);
}

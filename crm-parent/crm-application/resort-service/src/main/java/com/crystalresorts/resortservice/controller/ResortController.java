// com.crystalresorts.resortservice.controller
package com.crystalresorts.resortservice.controller;

import com.crystalresorts.resortservice.dto.ResortRequest;
import com.crystalresorts.resortservice.dto.ResortResponse;
import com.crystalresorts.resortservice.service.ResortService;
import com.crystalresorts.resortservice.validation.PositiveId;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/resorts")
@RequiredArgsConstructor
@Validated
public class ResortController {

    private final ResortService resortService;

//    @PostMapping
//     public ResponseEntity<ResortResponse> create(@Valid @RequestBody ResortRequest request) {
//         return ResponseEntity
//                 .status(HttpStatus.CREATED)
//                 .body(resortService.createResort(request));
//     }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResortResponse> create(
            @Valid @RequestBody ResortRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resortService.createResort(request));
    }


    @GetMapping
    public ResponseEntity<List<ResortResponse>> getAll() {
        return ResponseEntity.ok(resortService.getAllResorts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResortResponse> getById(@PositiveId @PathVariable Long id) {
        return ResponseEntity.ok(resortService.getResortById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PositiveId @PathVariable Long id) {
        resortService.deleteResort(id);
        return ResponseEntity.noContent().build();
    }

}
package com.sista.georeportapi.controller;

import com.sista.georeportapi.dto.ObservationRequest;
import com.sista.georeportapi.dto.ObservationResponse;
import com.sista.georeportapi.model.Observation;
import com.sista.georeportapi.model.ObservationType;
import com.sista.georeportapi.service.ObservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/observations")
public class ObservationController {

    private final ObservationService observationService;

    @PostMapping
    public ResponseEntity<ObservationResponse> create(@Valid @RequestBody ObservationRequest observationRequest) {
        Observation observation = observationService.create(observationRequest);
        return ResponseEntity.ok(toRespone(observation));
    }

    @GetMapping()
    public List<ObservationResponse> observations(ObservationType type, Instant from, Instant to, String box) {
        double [] boxArr = parseBox(box);

        return observationService.observations(type, from, to, boxArr)
                .stream()
                .map(this::toRespone)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObservationResponse> read(@PathVariable UUID id) {
        return observationService.get(id)
                .map( o -> ResponseEntity.ok(toRespone(o)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        observationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //--------------------------------------Private Methods----------------------------------------------------//

    private ObservationResponse toRespone(Observation observation) {
        return ObservationResponse.builder()
                .id(observation.getId())
                .titre(observation.getTitre())
                .type(observation.getType())
                .description(observation.getDescription())
                .lat(observation.getLat())
                .lon(observation.getLon())
                .observationAt(observation.getCreatedAt())
                .source(observation.getSource())
                .createdAt(observation.getCreatedAt())
                .build();
    }

    private double[] parseBox(String box) {
        if (box == null || box.isBlank()) return null;
        String[] parts = box.split(",");
        if (parts.length != 4) throw new IllegalArgumentException("box must be minLon,minLat,maxLon,maxLat");
        return new double[] {
                Double.parseDouble(parts[0].trim()),
                Double.parseDouble(parts[1].trim()),
                Double.parseDouble(parts[2].trim()),
                Double.parseDouble(parts[3].trim())
        };
    }
}

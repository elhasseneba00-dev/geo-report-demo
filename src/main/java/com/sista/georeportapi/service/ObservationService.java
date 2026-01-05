package com.sista.georeportapi.service;

import com.sista.georeportapi.dto.ObservationRequest;
import com.sista.georeportapi.model.Observation;
import com.sista.georeportapi.model.ObservationType;
import com.sista.georeportapi.repository.ObservationRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObservationService {
    private final ObservationRepository observationRepository;

    // Create an observation
    public Observation create(ObservationRequest request) {
        Observation observation = Observation.builder()
                .titre(request.getTitre())
                .type(request.getType())
                .description(request.getDescription())
                .lat(request.getLat())
                .lon(request.getLon())
                .observationAt(request.getObservationAt())
                .source(request.getSource())
                .build();
        return observationRepository.save(observation);
    }

    public Optional<Observation> get(UUID id) {
        return observationRepository.findById(id);
    }

    public List<Observation> observations(ObservationType type, Instant from, Instant to, double[] box) {
        Instant fromI = from != null ? from : Instant.EPOCH;
        Instant toI = to != null ? to : Instant.now();

        List<Observation> base;
        if (type != null) base = observationRepository.findByTypeAndObservationAtBetween(type, fromI, toI);
        else base = observationRepository.findByObservationAtBetween(fromI, toI);

        if (box == null) return base;

        double minLon = box[0], minLat = box[1], maxLon= box[2], maxLat = box[2];

        return base.stream()
                .filter(o -> o.getLat() >= minLat && o.getLat() <= maxLat
                        && o.getLon() >= minLon && o.getLon() <= maxLon)
                .collect(Collectors.toList());

    }

    public void delete(UUID id){
        observationRepository.deleteById(id);
    }

    public Map<ObservationType, Long> countByType(List<Observation> observations) {
        return observations.stream()
                .collect(Collectors.groupingBy(Observation::getType, Collectors.counting()));
    }




}

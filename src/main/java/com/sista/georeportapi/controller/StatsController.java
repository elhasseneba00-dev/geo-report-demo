package com.sista.georeportapi.controller;

import com.sista.georeportapi.model.Observation;
import com.sista.georeportapi.model.ObservationType;
import com.sista.georeportapi.service.ObservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final ObservationService observationService;

    @GetMapping("/observation-par-type")
    public Map<ObservationType, Long> observationParType(Instant from, Instant to) {
        List<Observation> all = observationService.observations(null, from, to, null);
        return observationService.countByType(all);
    }
}

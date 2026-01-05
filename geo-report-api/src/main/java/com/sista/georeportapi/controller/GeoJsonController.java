package com.sista.georeportapi.controller;

import com.sista.georeportapi.model.Observation;
import com.sista.georeportapi.model.ObservationType;
import com.sista.georeportapi.service.ObservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/observations/geo-json")
@RequiredArgsConstructor
public class GeoJsonController {

    private final ObservationService observationService;

    @GetMapping
    public Map<String, Object> geoJson(ObservationType type, Instant from, Instant to, String box) {
        double[] boxArr = parseBox(box);
        List<Observation> observations = observationService.observations(type, from, to, boxArr);

        // GeoJSON standard (RFC 7946)
        Map<String, Object> fc = new LinkedHashMap<>();
        fc.put("type", "FeatureCollection");

        List<Map<String, Object>> features = new ArrayList<>();
        for (Observation observation : observations) {
            Map<String, Object> feature = new LinkedHashMap<>();
            feature.put("type", "Feature");

            Map<String, Object> geometry = new LinkedHashMap<>();
            geometry.put("type", "Point");
            geometry.put("coordinates", List.of(observation.getLon(), observation.getLat())); // GeoJSON = [lon, lat]
            feature.put("geometry", geometry);

            Map<String, Object> properties = new LinkedHashMap<>();
            properties.put("id", observation.getId() != null ? observation.getId().toString() : null);
            properties.put("titre", observation.getTitre());
            properties.put("type", observation.getType());
            properties.put("source", observation.getSource());
            properties.put("observationAt", observation.getObservationAt());
            properties.put("createdAt", observation.getCreatedAt());

            feature.put("properties", properties);

            features.add(feature);
        }

        fc.put("features", features);
        return fc;
    }

    private double[] parseBox(String box) {
        if (box == null || box.isBlank()) return null;
        String[] parts = box.split(",");
        if (parts.length != 4) throw new IllegalArgumentException("box doit être en: minLon,minLat,maxLon,maxLat");
        return new double[] {
                Double.parseDouble(parts[0].trim()),
                Double.parseDouble(parts[1].trim()),
                Double.parseDouble(parts[2].trim()),
                Double.parseDouble(parts[3].trim())
        };
    }
}

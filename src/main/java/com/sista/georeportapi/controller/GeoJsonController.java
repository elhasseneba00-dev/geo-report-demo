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
    public Map<String, Object> geoJson(ObservationType type, Instant from, Instant to, String box){
        double[] boxArr = parseBox(box);
        List<Observation> observations = observationService.observations(type, from, to, boxArr);

        Map<String, Object> fc = new LinkedHashMap<>();
        fc.put("Type", "Collection de fonctionnalités");

        List<Map<String, Object>> features = new ArrayList<>();
        for(Observation observation : observations){
            Map<String, Object> feature = new LinkedHashMap<>();
            feature.put("Type", "Fonctionnalité");

            Map<String, Object> geometry = new LinkedHashMap<>();
            geometry.put("Type", "Point");
            geometry.put("Coordonnées", List.of(observation.getLon(), observation.getLat())); // GeoJSON = [lon, lat]
            feature.put("Géométry", geometry);


            Map<String, Object> properties = new LinkedHashMap<>();
            properties.put("id", observation.getId().toString());
            properties.put("titre", observation.getTitre());
            properties.put("type", observation.getType());
            properties.put("source", observation.getSource());
            properties.put("obsertionAt", observation.getObservationAt());
            properties.put("createdAt", observation.getCreatedAt());

            feature.put("Propriétés", properties);

            features.add(feature);
        }

        fc.put("Fonctionnalités", features);
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

package com.sista.georeportapi.dto;

import com.sista.georeportapi.model.ObservationType;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class ObservationResponse {
    UUID id;
    String titre;
    ObservationType type;
    String description;
    double lat;
    double lon;
    Instant observationAt;
    String source;
    Instant createdAt;
}

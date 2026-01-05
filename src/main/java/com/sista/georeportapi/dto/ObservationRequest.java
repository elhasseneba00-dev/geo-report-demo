package com.sista.georeportapi.dto;

import com.sista.georeportapi.model.ObservationType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Data
public class ObservationRequest {

    @NotBlank
    private String titre;
    @NotNull
    private ObservationType type;
    private String description;

    @NotNull
    @DecimalMax(value = "90.0")
    @DecimalMin(value = "-90.0")
    private double lat;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private double lon;

    private Instant observationAt;

    @NotBlank
    private String source;
}

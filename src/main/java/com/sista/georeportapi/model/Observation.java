package com.sista.georeportapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "observations")
@Builder @Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Observation {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ObservationType type;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lon;

    @Column(nullable = false)
    private Instant observationAt;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
        if (observationAt == null) observationAt = createdAt;
        if (source == null || source.isBlank()) source = "manuelle";
    }
}

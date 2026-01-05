package com.sista.georeportapi.repository;

import com.sista.georeportapi.model.Observation;
import com.sista.georeportapi.model.ObservationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ObservationRepository extends JpaRepository<Observation, UUID> {
    List<Observation> findByTypeAndObservationAtBetween(ObservationType type, Instant fromI, Instant toI);

    List<Observation> findByObservationAtBetween(Instant fromI, Instant toI);
}

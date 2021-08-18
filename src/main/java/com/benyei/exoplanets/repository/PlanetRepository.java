package com.benyei.exoplanets.repository;

import com.benyei.exoplanets.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanetRepository extends JpaRepository<Planet, Long> {
    Optional<Planet> findPlanetByName(String name);
    List<Planet> findAllByIsInTheHabitableZoneTrue();
}

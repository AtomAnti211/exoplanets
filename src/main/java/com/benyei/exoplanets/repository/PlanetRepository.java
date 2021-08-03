package com.benyei.exoplanets.repository;

import com.benyei.exoplanets.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetRepository extends JpaRepository<Planet, Long> {
}

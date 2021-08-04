package com.benyei.exoplanets.service;

import com.benyei.exoplanets.model.Planet;

import java.util.List;

public interface PlanetService {

    Planet savePlanet(Planet employee);
    List<Planet> getAllPlanets();
    Planet getPlanetById(long id);
    Planet updatePlanet(Planet employee, long id);
    void deletePlanet(long id);
   // List<Planet> findAllByIsInTheHabitableZoneTrue();
}

package com.benyei.exoplanets.service;

import com.benyei.exoplanets.model.Planet;

import java.util.List;

public interface PlanetService {

    Planet savePlanet(Planet planet);
    List<Planet> getAllPlanets();
    Planet getPlanetById(long id);
    Planet updatePlanet(Planet planet, long id);
    void deletePlanet(long id);
}

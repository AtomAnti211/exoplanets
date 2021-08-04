package com.benyei.exoplanets.service.Impl;

import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Planet;
import com.benyei.exoplanets.repository.PlanetRepository;
import com.benyei.exoplanets.service.PlanetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlanetServiceImpl implements PlanetService {


    private PlanetRepository planetRepository;

    public PlanetServiceImpl(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @Override
    public Planet savePlanet(Planet planet) {
        return planetRepository.save(planet);
    }

    @Override
    public List<Planet> getAllPlanets() {
        return planetRepository.findAll();
    }

    @Override
    public Planet getPlanetById(long id) {
        return planetRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Planet", "Id", id));

    }

    @Override
    public Planet updatePlanet(Planet planet, long id) {

        Planet existingPlanet = planetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Planet", "Id", id));

        existingPlanet.setName(planet.getName());
        existingPlanet.setYearOfDiscovery(planet.getYearOfDiscovery());
        existingPlanet.setDetection(planet.getDetection());
        existingPlanet.setStatusIsConfirmed(planet.getStatusIsConfirmed());
        existingPlanet.setStar(planet.getStar());
        planetRepository.save(existingPlanet);
        return existingPlanet;
    }

    @Override
    public void deletePlanet(long id) {
        planetRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Planet", "Id", id));
        planetRepository.deleteById(id);
    }
}

package com.benyei.exoplanets.service.Impl;

import com.benyei.exoplanets.exception.NotUniqueException;
import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Moon;
import com.benyei.exoplanets.repository.MoonRepository;
import com.benyei.exoplanets.service.MoonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MoonServiceImpl implements MoonService {

    private final MoonRepository moonRepository;

    public MoonServiceImpl(MoonRepository moonRepository) {
        super();
        this.moonRepository = moonRepository;
    }

    @Override
    public Moon saveMoon(Moon moon) {
        Optional<Moon> existingMoon = moonRepository.findMoonByName(moon.getName());
        if (existingMoon.isPresent()) {
            throw new NotUniqueException("Moon already exists with this name: " + moon.getName());
        }
        return moonRepository.save(moon);
    }

    @Override
    public List<Moon> getAllMoons() {
        if (moonRepository.findAll().isEmpty()) {
            throw new ResourceNotFoundException("Not Found!");
        }
        return moonRepository.findAll();
    }

    @Override
    public Moon getMoonById(long id) {
        return moonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Moon not found with id: " + id));
    }

    @Override
    public Moon updateMoon(Moon moon, long id) {

        Optional<Moon> optionalMoon = moonRepository.findMoonByName(moon.getName());
        if (optionalMoon.isPresent()) {
            throw new NotUniqueException("Moon already exists with this name: " + moon.getName());
        }

        Moon existingMoon = moonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Moon not found with id: " + id));

        existingMoon.setName(moon.getName());
        existingMoon.setPlanet(moon.getPlanet());
        existingMoon.setStatus(moon.getStatus());
        moonRepository.save(existingMoon);
        return existingMoon;
    }

    @Override
    public void deleteMoon(long id) {
        moonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Moon not found with id: " + id));
        moonRepository.deleteById(id);
    }
}

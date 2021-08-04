package com.benyei.exoplanets.service.Impl;

import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Moon;
import com.benyei.exoplanets.repository.MoonRepository;
import com.benyei.exoplanets.service.MoonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return moonRepository.save(moon);
    }

    @Override
    public List<Moon> getAllMoons() {
        return moonRepository.findAll();
    }

    @Override
    public Moon getMoonById(long id) {
        return moonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Moon", "Id", id));

    }

    @Override
    public Moon updateMoon(Moon moon, long id) {
        Moon existingMoon = moonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Moon", "Id", id));

        existingMoon.setName(moon.getName());
        existingMoon.setPlanet(moon.getPlanet());
        existingMoon.setStatus(moon.getStatus());
        moonRepository.save(existingMoon);
        return existingMoon;
    }

    @Override
    public void deleteMoon(long id) {
        moonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Moon", "Id", id));
        moonRepository.deleteById(id);
    }
}

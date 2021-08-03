package com.benyei.exoplanets.service.Impl;

import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Star;
import com.benyei.exoplanets.repository.StarRepository;
import com.benyei.exoplanets.service.StarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StarServiceImpl implements StarService {

    private final StarRepository starRepository;

    public StarServiceImpl(StarRepository starRepository) {
        this.starRepository = starRepository;
    }

    @Override
    public Star saveStar(Star star) {
        return starRepository.save(star);
    }

    @Override
    public List<Star> getAllStars() {
        return starRepository.findAll();
    }

    @Override
    public Star getStarById(long id) {
        return starRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Star", "Id", id));
    }

    @Override
    public Star updateStar(Star star, long id) {
        Star existingStar = starRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Star", "Id", id));
        existingStar.setName(star.getName());
        existingStar.setMassSun(star.getMassSun());
        existingStar.setRadiusSun(star.getRadiusSun());
        existingStar.setTemperature(star.getTemperature());
        existingStar.setDistanceInLightYears(star.getDistanceInLightYears());
        starRepository.save(existingStar);
        return existingStar;
    }

    @Override
    public void deleteStar(long id) {
        starRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Star", "Id", id));
        starRepository.deleteById(id);
    }
}

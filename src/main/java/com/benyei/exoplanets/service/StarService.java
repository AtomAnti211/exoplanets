package com.benyei.exoplanets.service;


import com.benyei.exoplanets.exception.ConstraintException;
import com.benyei.exoplanets.exception.NotUniqueException;
import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Star;
import com.benyei.exoplanets.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StarService {

    @Autowired
    private StarRepository starRepository;

    public Star saveStar(Star star) {
        Optional<Star> existingStar = starRepository.findStarByName(star.getName());
        if (existingStar.isPresent()) {
            throw new NotUniqueException("Star already exists with this name: " + star.getName());
        }
        return starRepository.save(star);
    }

    public List<Star> getAllStars() {
        if (starRepository.findAll().isEmpty()) {
            throw new ResourceNotFoundException("Not Found!");
        }
        return starRepository.findAll();
    }

    public Star getStarById(long id) {
        return starRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("With this ID: " + id + ", star does not exist."));
    }

    public Star updateStar(Star star, long id) {
        Optional<Star> optionalStar = starRepository.findStarByName(star.getName());
        if (optionalStar.isPresent() && optionalStar.get().getId() != id) {
            throw new NotUniqueException(
                    "A star with this name already exists under this name " + star.getName() +
                            " and not this identifier: " + id);
        }
        Star existingStar = starRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Star not found with id: " + id));
        existingStar.setName(star.getName());
        existingStar.setMassSun(star.getMassSun());
        existingStar.setRadiusSun(star.getRadiusSun());
        existingStar.setTemperature(star.getTemperature());
        existingStar.setDistanceInLightYears(star.getDistanceInLightYears());
        starRepository.save(existingStar);
        return existingStar;
    }

    public void deleteStar(long id) throws ConstraintException, ResourceNotFoundException {
        Star existingStar = starRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("With this ID: " + id + ", star does not exist."));

        if (existingStar.getPlanets().size() > 0) {
            throw new ConstraintException("This star (id: "
                    + id + ") has a planet(s), so it cannot be deleted!");
        }
        starRepository.deleteById(id);
    }
}

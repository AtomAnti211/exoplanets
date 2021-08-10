package com.benyei.exoplanets.service.Impl;

import com.benyei.exoplanets.exception.ConstraintException;
import com.benyei.exoplanets.exception.NotUniqueException;
import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Star;
import com.benyei.exoplanets.repository.StarRepository;
import com.benyei.exoplanets.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StarServiceImpl implements StarService {

    @Autowired
    private StarRepository starRepository;

    @Override
    public Star saveStar(Star star) {
        Optional<Star> existingStar = starRepository.findStarByName(star.getName());
        if (existingStar.isPresent()) {
            throw new NotUniqueException("Star already exists with this name: " + star.getName());
        }
        return starRepository.save(star);
    }

    @Override
    public List<Star> getAllStars() {
        if (starRepository.findAll().isEmpty()) {
            throw new ResourceNotFoundException("Not Found!");
        }
        return starRepository.findAll();
    }

    @Override
    public Star getStarById(long id) {
        return starRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Star not found with id: " + id));
    }

    @Override
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

    @Override
    public void deleteStar(long id) throws ConstraintException, ResourceNotFoundException{
        Star existingStar = starRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Star not found with id: " + id));

        if (existingStar.getPlanets().size() > 0) {
            throw new ConstraintException("This star (id: "
                    + id + ") has a planet(s), so it cannot be deleted!");
        }
        starRepository.deleteById(id);
    }
}

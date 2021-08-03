package com.benyei.exoplanets.service.Impl;

import com.benyei.exoplanets.model.Star;
import com.benyei.exoplanets.repository.StarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StarServiceImpl {

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
}

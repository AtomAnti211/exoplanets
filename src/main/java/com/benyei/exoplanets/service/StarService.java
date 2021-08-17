package com.benyei.exoplanets.service;

import com.benyei.exoplanets.model.Star;

import java.util.List;

public interface StarService {

    Star saveStar(Star star);
    List<Star> getAllStars();
    Star getStarById(long id);
    Star updateStar(Star star, long id);
    void deleteStar(long id);
}

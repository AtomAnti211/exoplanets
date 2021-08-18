package com.benyei.exoplanets.repository;

import com.benyei.exoplanets.model.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StarRepository extends JpaRepository<Star, Long> {
    Optional<Star> findStarByName(String name);
    List<Star> findAllByDistanceInLightYearsIsLessThan(Double distance);
    List<Star> findAllByNameContaining(Optional<String> name);
}

package com.benyei.exoplanets.repository;

import com.benyei.exoplanets.model.Star;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRepository extends JpaRepository<Star, Long> {
}

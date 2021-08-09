package com.benyei.exoplanets.repository;

import com.benyei.exoplanets.model.Moon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoonRepository extends JpaRepository<Moon, Long> {
    Optional<Moon> findMoonByName(String name);
}

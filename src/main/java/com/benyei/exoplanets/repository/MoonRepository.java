package com.benyei.exoplanets.repository;

import com.benyei.exoplanets.model.Moon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoonRepository extends JpaRepository<Moon, Long> {
}

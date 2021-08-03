package com.benyei.exoplanets.service;

import com.benyei.exoplanets.model.Moon;

import java.util.List;

public interface MoonService {

    Moon saveMoon(Moon employee);
    List<Moon> getAllMoons();
    Moon getMoonById(long id);
    Moon updateMoon(Moon employee, long id);
    void deleteMoon(long id);
}

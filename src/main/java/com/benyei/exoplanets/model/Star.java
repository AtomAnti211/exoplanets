package com.benyei.exoplanets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Table(name = "Stars")
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, message = "The star name cannot be shorter than two character!")
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Positive(message = "The temperature can't be negative or zero!")
    @Column(name = "temperature_in_Kelvin")
    private Integer temperature;

    @Positive(message = "The distance can't be negative or zero!")
    private Double distanceInLightYears;

    @Positive(message = "The radius can't be negative or zero!")
    private Double radiusSun;

    @Positive(message = "The mass of all stars is always positive!")
    private Double massSun;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "star")
    @JsonIgnore
    private List<Planet> planets;

    public Star() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Double getDistanceInLightYears() {
        return distanceInLightYears;
    }

    public void setDistanceInLightYears(Double distanceInLightYears) {
        this.distanceInLightYears = distanceInLightYears;
    }

    public Double getRadiusSun() {
        return radiusSun;
    }

    public void setRadiusSun(Double radiusSun) {
        this.radiusSun = radiusSun;
    }

    public Double getMassSun() {
        return massSun;
    }

    public void setMassSun(Double massSun) {
        this.massSun = massSun;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }
}

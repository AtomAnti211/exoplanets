package com.benyei.exoplanets.model;

import com.benyei.exoplanets.annotation.ValidMaxYearValue;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Table(name = "planets")
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @Size(min = 2, message = "The planet name cannot be shorter than two character!")
    private String name;

    @Column(name = "YEAR_OF_DISCOVERY")
    @Min(value = 1995, message = "The first exoplanet was discovered in 1995 (confirmed), please give me a real year!")
    @ValidMaxYearValue
    private Integer yearOfDiscovery;

    @Enumerated(EnumType.STRING)
    private Detection detection;

    @Column(name = "confirmed")
    private Boolean statusIsConfirmed;

    @Column(name = "Habitable_Zone")
    private Boolean isInTheHabitableZone;

    @ManyToOne
    private Star star;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true, mappedBy = "planet")
    private List<Moon> moons;

    public Planet() {
    }

    public Planet(Long id, String name, Integer yearOfDiscovery, Detection detection, Boolean statusIsConfirmed, Boolean isInTheHabitableZone, Star star) {
        this.id = id;
        this.name = name;
        this.yearOfDiscovery = yearOfDiscovery;
        this.detection = detection;
        this.statusIsConfirmed = statusIsConfirmed;
        this.isInTheHabitableZone = isInTheHabitableZone;
        this.star = star;
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

    public Integer getYearOfDiscovery() {
        return yearOfDiscovery;
    }

    public void setYearOfDiscovery(Integer yearOfDiscovery) {
        this.yearOfDiscovery = yearOfDiscovery;
    }

    public Detection getDetection() {
        return detection;
    }

    public void setDetection(Detection detection) {
        this.detection = detection;
    }

    public Boolean getStatusIsConfirmed() {
        return statusIsConfirmed;
    }

    public void setStatusIsConfirmed(Boolean statusIsConfirmed) {
        this.statusIsConfirmed = statusIsConfirmed;
    }

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }

    public List<Moon> getMoons() {
        return moons;
    }

    public void setMoons(List<Moon> moons) {
        this.moons = moons;
    }

    public Boolean getInTheHabitableZone() {
        return isInTheHabitableZone;
    }

    public void setInTheHabitableZone(Boolean inTheHabitableZone) {
        isInTheHabitableZone = inTheHabitableZone;
    }
}

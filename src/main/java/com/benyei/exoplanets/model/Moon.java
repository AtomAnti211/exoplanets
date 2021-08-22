package com.benyei.exoplanets.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Moons")
public class Moon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @Size(min = 2, message = "The moon name cannot be shorter than two character!")
    private String name;

    @Column(name = "confirmed")
    private Boolean status;

    @ManyToOne
    private Planet planet;

    public Moon() {
    }

    public Moon(Long id, String name, Boolean status, Planet planet) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.planet = planet;
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

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

package com.f1insight.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String nationality;
    private String teamColor;
    private String countryFlag;
    private String headquarters;
    private String teamPrincipal;
    private Integer firstEntry;
    private String logoUrl;

    @OneToMany(mappedBy = "teamEntity", fetch = FetchType.LAZY)
    private List<Driver> drivers = new ArrayList<>();

    // Transient fields for calculated stats (not persisted)
    @Transient
    private int totalPoints;

    @Transient
    private int totalWins;

    @Transient
    private int totalPodiums;

    public Team() {
    }

    public Team(String name) {
        this.name = name;
    }

    // Getters and Setters
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
    }

    public String getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getTeamPrincipal() {
        return teamPrincipal;
    }

    public void setTeamPrincipal(String teamPrincipal) {
        this.teamPrincipal = teamPrincipal;
    }

    public Integer getFirstEntry() {
        return firstEntry;
    }

    public void setFirstEntry(Integer firstEntry) {
        this.firstEntry = firstEntry;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getTotalPodiums() {
        return totalPodiums;
    }

    public void setTotalPodiums(int totalPodiums) {
        this.totalPodiums = totalPodiums;
    }
}

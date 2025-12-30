package com.f1insight.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "races")
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer season;
    private Integer round; // Yarış sırası (1-24)
    private String raceName; // "Bahrain Grand Prix"
    private LocalDate raceDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "circuit_id")
    private Circuit circuit;

    // 2024 yarış sonuçları
    private String winnerName;
    private String winnerTeam;
    private String winnerImageUrl;
    private String fastestLapTime;
    private String fastestLapDriver;

    public Race() {
    }

    public Race(Integer season, Integer round, String raceName) {
        this.season = season;
        this.round = round;
        this.raceName = raceName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public LocalDate getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(LocalDate raceDate) {
        this.raceDate = raceDate;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getWinnerTeam() {
        return winnerTeam;
    }

    public void setWinnerTeam(String winnerTeam) {
        this.winnerTeam = winnerTeam;
    }

    public String getWinnerImageUrl() {
        return winnerImageUrl;
    }

    public void setWinnerImageUrl(String winnerImageUrl) {
        this.winnerImageUrl = winnerImageUrl;
    }

    public String getFastestLapTime() {
        return fastestLapTime;
    }

    public void setFastestLapTime(String fastestLapTime) {
        this.fastestLapTime = fastestLapTime;
    }

    public String getFastestLapDriver() {
        return fastestLapDriver;
    }

    public void setFastestLapDriver(String fastestLapDriver) {
        this.fastestLapDriver = fastestLapDriver;
    }
}

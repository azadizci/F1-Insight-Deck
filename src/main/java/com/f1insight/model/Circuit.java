package com.f1insight.model;

import jakarta.persistence.*;

@Entity
@Table(name = "circuits")
public class Circuit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String circuitId; // API'den (örn: "monaco")

    private String name; // Pist adı
    private String country; // Ülke
    private String locality; // Şehir
    private String countryFlag; // Emoji bayrak

    // Statik metadata
    private Double lengthKm; // Pist uzunluğu (km)
    private Integer laps; // Tur sayısı
    private String lapRecordTime; // All-time tur rekoru
    private String lapRecordHolder; // Rekor sahibi
    private Integer lapRecordYear; // Rekorun kırıldığı yıl

    public Circuit() {
    }

    public Circuit(String circuitId, String name) {
        this.circuitId = circuitId;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(String circuitId) {
        this.circuitId = circuitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
    }

    public Double getLengthKm() {
        return lengthKm;
    }

    public void setLengthKm(Double lengthKm) {
        this.lengthKm = lengthKm;
    }

    public Integer getLaps() {
        return laps;
    }

    public void setLaps(Integer laps) {
        this.laps = laps;
    }

    public String getLapRecordTime() {
        return lapRecordTime;
    }

    public void setLapRecordTime(String lapRecordTime) {
        this.lapRecordTime = lapRecordTime;
    }

    public String getLapRecordHolder() {
        return lapRecordHolder;
    }

    public void setLapRecordHolder(String lapRecordHolder) {
        this.lapRecordHolder = lapRecordHolder;
    }

    public Integer getLapRecordYear() {
        return lapRecordYear;
    }

    public void setLapRecordYear(Integer lapRecordYear) {
        this.lapRecordYear = lapRecordYear;
    }

}

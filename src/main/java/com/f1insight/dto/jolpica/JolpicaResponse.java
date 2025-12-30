package com.f1insight.dto.jolpica;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JolpicaResponse {
    @JsonProperty("MRData")
    private MRData mrData;

    public MRData getMrData() {
        return mrData;
    }

    public void setMrData(MRData mrData) {
        this.mrData = mrData;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MRData {
        @JsonProperty("StandingsTable")
        private StandingsTable standingsTable;

        public StandingsTable getStandingsTable() {
            return standingsTable;
        }

        public void setStandingsTable(StandingsTable standingsTable) {
            this.standingsTable = standingsTable;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StandingsTable {
        @JsonProperty("StandingsLists")
        private List<StandingsList> standingsLists;

        public List<StandingsList> getStandingsLists() {
            return standingsLists;
        }

        public void setStandingsLists(List<StandingsList> standingsLists) {
            this.standingsLists = standingsLists;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StandingsList {
        @JsonProperty("season")
        private String season;

        @JsonProperty("DriverStandings")
        private List<DriverStanding> driverStandings;

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public List<DriverStanding> getDriverStandings() {
            return driverStandings;
        }

        public void setDriverStandings(List<DriverStanding> driverStandings) {
            this.driverStandings = driverStandings;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DriverStanding {
        private String position;
        private String points;
        private String wins;

        @JsonProperty("Driver")
        private DriverDto driver;

        @JsonProperty("Constructors")
        private List<ConstructorDto> constructors;

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getWins() {
            return wins;
        }

        public void setWins(String wins) {
            this.wins = wins;
        }

        public DriverDto getDriver() {
            return driver;
        }

        public void setDriver(DriverDto driver) {
            this.driver = driver;
        }

        public List<ConstructorDto> getConstructors() {
            return constructors;
        }

        public void setConstructors(List<ConstructorDto> constructors) {
            this.constructors = constructors;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DriverDto {
        private String driverId;
        private String permanentNumber;
        private String code;
        private String url;
        private String givenName;
        private String familyName;
        private String dateOfBirth;
        private String nationality;

        public String getDriverId() {
            return driverId;
        }

        public void setDriverId(String driverId) {
            this.driverId = driverId;
        }

        public String getGivenName() {
            return givenName;
        }

        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConstructorDto {
        private String constructorId;
        private String url;
        private String name;
        private String nationality;

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
    }
}

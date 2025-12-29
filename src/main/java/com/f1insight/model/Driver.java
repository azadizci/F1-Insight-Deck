package com.f1insight.model;

import java.util.List;

public class Driver {
    private Long id;
    private String name;
    private String team;
    private int wins;
    private int podiums;
    private int points;
    private String imageUrl;
    private String country;
    private String countryFlag;
    private String teamLogo;
    private String teamColor;
    private List<RaceResult> lastTenRaces;

    public Driver() {}

    public Driver(Long id, String name, String team, int wins, int podiums, int points,
                  String imageUrl, String country, String countryFlag, String teamLogo, String teamColor) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.wins = wins;
        this.podiums = podiums;
        this.points = points;
        this.imageUrl = imageUrl;
        this.country = country;
        this.countryFlag = countryFlag;
        this.teamLogo = teamLogo;
        this.teamColor = teamColor;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public int getPodiums() { return podiums; }
    public void setPodiums(int podiums) { this.podiums = podiums; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCountryFlag() { return countryFlag; }
    public void setCountryFlag(String countryFlag) { this.countryFlag = countryFlag; }

    public String getTeamLogo() { return teamLogo; }
    public void setTeamLogo(String teamLogo) { this.teamLogo = teamLogo; }

    public String getTeamColor() { return teamColor; }
    public void setTeamColor(String teamColor) { this.teamColor = teamColor; }

    public List<RaceResult> getLastTenRaces() { return lastTenRaces; }
    public void setLastTenRaces(List<RaceResult> lastTenRaces) { this.lastTenRaces = lastTenRaces; }
}

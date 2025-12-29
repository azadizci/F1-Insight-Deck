package com.f1insight.model;

public class RaceResult {
    private String raceName;
    private int position;

    public RaceResult() {}

    public RaceResult(String raceName, int position) {
        this.raceName = raceName;
        this.position = position;
    }

    public String getRaceName() { return raceName; }
    public void setRaceName(String raceName) { this.raceName = raceName; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
}

package com.example.myapplication.objects;

public class Record {

    private String nickname;
    private int score;
    private double lat;
    private double lng;
    private String dateFormat;
    private boolean sensorsMode;

    public Record() {
    }

    public String getNickname() {
        return nickname;
    }

    public Record setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Record setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Record setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Record setLng(double lng) {
        this.lng = lng;
        return this;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public Record setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public boolean isSensorsMode() {
        return sensorsMode;
    }

    public Record setSensorsMode(boolean sensorsMode) {
        this.sensorsMode = sensorsMode;
        return this;
    }

    @Override
    public String toString() {
        return nickname + " - " + (sensorsMode == true ? "Sensors" : "Buttons")
                + "\n" + score + " pts, " + dateFormat;
    }

}
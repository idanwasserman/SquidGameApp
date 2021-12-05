package com.example.myapplication.objects;

import java.util.Date;

public class Record {

    private String nickname;
    private int score;
    private double lat;
    private double lng;
    private Date date;

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

    public Date getDate() {
        return date;
    }

    public Record setDate(Date date) {
        this.date = date;
        return this;
    }

    @Override
    public String toString() {
        if (nickname == null)
            return score + " pts, " + date.getDay() + "/" + date.getMonth();
        return nickname + ": "  + score + " pts, " + date.getDay() + "/" + date.getMonth();
    }

}
package com.example.Girl_Power.Model;

public class Record implements Comparable<Record>{
    private int score = 0;
    private double lat = 0;
    private double lon = 0;
    private String date = "";

    public Record() {
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

    public double getLon() {
        return lon;
    }

    public Record setLon(double lon) {
        this.lon = lon;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Record setDate(String date) {
        this.date = date;
        return this;
    }

    @Override
    public int compareTo(Record otherRecord) {
        if (this.score > otherRecord.score)
            return 1;
        else if (this.score < otherRecord.score)
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return "Record{" +
                "score=" + score +
                ", lat=" + lat +
                ", lon=" + lon +
                ", date='" + date + '\'' +
                '}';
    }
}

package com.ec.model;

/**
 * Created by Anish on 4/14/2018.
 */

public class UserLatLong {
    double lat, lon;

    public UserLatLong() {
    }

    public UserLatLong(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}

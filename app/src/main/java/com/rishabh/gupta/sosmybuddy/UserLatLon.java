package com.rishabh.gupta.sosmybuddy;

public class UserLatLon {

    Double latitude;
    Double longitude;

    public UserLatLon() {
    }

    public UserLatLon(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

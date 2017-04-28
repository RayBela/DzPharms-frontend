package org.rayhane.dzpharmz.Model;

/**
 * Created by Rayhane on 25/04/2017.
 */

public class Address {

    protected String name;
    protected String location;
    protected double longitude;
    protected double latitude;

    public Address(String name, String location) {
        this.name = name;
        this.location = location;
    }


    public Address(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

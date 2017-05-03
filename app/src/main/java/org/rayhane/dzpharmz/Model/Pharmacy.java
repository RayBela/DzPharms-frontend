package org.rayhane.dzpharmz.Model;

/**
 * Created by Rayhane on 25/04/2017.
 */

public class Pharmacy {

    protected String name;
    protected String description;
    protected String pharmacy_address;
    protected double longitude;
    protected double latitude;
    protected String phone_number;
    protected String href;


    public Pharmacy() {
    }

    public String getPharmacy_address() {
        return pharmacy_address;
    }

    public Pharmacy(String name, String pharmacy_address, String href) {
        this.name = name;
        this.pharmacy_address = pharmacy_address;
        this.href = href;
    }

    public void setPharmacy_address(String pharmacy_address) {
        this.pharmacy_address = pharmacy_address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}

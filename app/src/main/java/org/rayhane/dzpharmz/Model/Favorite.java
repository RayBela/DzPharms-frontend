package org.rayhane.dzpharmz.Model;

/**
 * Created by Rayhane on 25/04/2017.
 */

public class Favorite {

    protected String name;
    protected String address;
    protected double longitude;
    protected double latitude;
    protected String time;
    protected String phone_number;



    public Favorite(String name, String address, String state) {
        this.name = name;
        this.address = address;
        this.time = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

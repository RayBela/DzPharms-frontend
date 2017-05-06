package org.rayhane.dzpharmz.Model;

/**
 * Created by Rayhane on 05/05/2017.
 */

public class PostBody {

    double latitude;
    double longitude;

    public PostBody() {
    }

    public PostBody(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

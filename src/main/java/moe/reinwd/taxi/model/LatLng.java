package moe.reinwd.taxi.model;

import java.io.Serializable;

/**
 * @author ReinWD 张巍 2016214874
 * @e-mail ReinWDD@gmail.com
 */
public class LatLng implements Serializable {
    private double lat;
    private double lng;
    LatLng (double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}

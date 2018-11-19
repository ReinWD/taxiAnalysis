package moe.reinwd.taxi.model;

import moe.reinwd.taxi.util.LocationUtils;

import java.io.Serializable;

/**
 * @author ReinWD 张巍 2016214874
 * @e-mail ReinWDD@gmail.com
 */
public class Region implements Serializable {
    private String name;
    private LatLng center;
    private double radius;

    private Region(String str) {
        String[] split = str.split(",");
        int i = 0;
        this.name = split[i++];
        double lng = Double.parseDouble(split[i++]);
        double lat = Double.parseDouble(split[i++]);
        this.center = new LatLng(lat, lng);
        this.radius = Double.parseDouble(split[i++]);
    }

    public boolean isInRegion(LatLng taxiPosition){
        double distance = LocationUtils.getDistance(taxiPosition, center);
        return distance<radius;
    }

    public static Region createFromStr(String str) {
        return new Region(str);
    }

    public String getName() {
        return name;
    }
}

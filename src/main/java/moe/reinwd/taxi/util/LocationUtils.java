package moe.reinwd.taxi.util;

import moe.reinwd.taxi.model.LatLng;

/**
 * copied from https://blog.csdn.net/sanyuesan0000/article/details/51683464
 * Created by yuliang on 2015/3/20.
 * modified by ReinWD on 2018/11/19
 */
public class LocationUtils {
    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：千米)
     * @return
     */
    public static double getDistance(LatLng latLng2, LatLng latLng1) {
        double radLat1 = rad(latLng1.getLat());
        double radLat2 = rad(latLng2.getLat());
        double a = radLat1 - radLat2;
        double b = rad(latLng1.getLng()) - rad(latLng2.getLng());
        double s = 2 * Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin(a / 2), 2)
                        + Math.cos(radLat1) * Math.cos(radLat2)
                        * Math.pow(Math.sin(b / 2), 2)
                )
        );
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
//        s = s*1000;
        return s;
    }
}
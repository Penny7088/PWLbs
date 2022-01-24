package com.pw.lib.lbs.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;

import com.pw.lib.lbs.entity.MapLatLng;

/**
 * com.hellotalk.lib.map.util
 *
 * @author : Penny (penny@hellotalk.com)
 * @describe :
 * @date : 4/2/21
 */
public class MapUtils {

    public static final double LATITUDE_INVALID = 361;
    public static final double LONGITUDE_INVALID = 361;

    public static String obtainChannel(Context context) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo.metaData.getString("HT_CHANNEL_NO");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static double calculatingRadiusDistance(MapLatLng farRight, MapLatLng farLeft, MapLatLng nearRight, MapLatLng nearLeft) {
        float[] distanceWidth = new float[2];
        float[] distanceHeight = new float[2];

        Location.distanceBetween(
                (farRight.getLatitude() + nearRight.getLatitude()) / 2,
                (farRight.getLongitude() + nearRight.getLongitude()) / 2,
                (farLeft.getLatitude() + nearLeft.getLatitude()) / 2,
                (farLeft.getLongitude() + nearLeft.getLongitude()) / 2,
                distanceWidth
        );

        Location.distanceBetween(
                (farRight.getLatitude() + nearRight.getLatitude()) / 2,
                (farRight.getLongitude() + nearRight.getLongitude()) / 2,
                (farLeft.getLatitude() + nearLeft.getLatitude()) / 2,
                (farLeft.getLongitude() + nearLeft.getLongitude()) / 2,
                distanceHeight
        );

        float distance = Math.max(distanceWidth[0], distanceHeight[0]);

        return distance / 1000;


    }

    public static boolean isLocationValid(double lat, double lng) {
        return lat != LATITUDE_INVALID && lng != LONGITUDE_INVALID && lat != 0 && lng != 0;
    }


}

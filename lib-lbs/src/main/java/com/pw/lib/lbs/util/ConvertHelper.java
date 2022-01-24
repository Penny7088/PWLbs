package com.pw.lib.lbs.util;

import com.pw.lib.lbs.entity.MapCameraPosition;
import com.pw.lib.lbs.entity.MapLatLng;
import com.pw.lib.lbs.entity.MapLatLngBounds;
import com.pw.lib.lbs.entity.MapMarker;
import com.pw.lib.lbs.entity.MapVisibleRegion;

/**
 * com.hellotalk.lib.map.util
 *
 * @author : Penny (penny@hellotalk.com)
 * @describe : 高德，谷歌api 转成 lib-map 自己的实体
 * @date : 4/7/21
 */
public class ConvertHelper {

    public MapLatLng convertMapLatLng(double lat, double lng) {
        return new MapLatLng(lat, lng);
    }

    public MapCameraPosition convertMapCameraPosition(double lat, double lng, float zoom, float tilt, float bearing) {
        MapLatLng mapLatLng = new MapLatLng(lat, lng);
        return new MapCameraPosition(mapLatLng, zoom, tilt, bearing);
    }

    public MapVisibleRegion convertMapVisibleRegion(MapLatLng nearLeft,
                                                    MapLatLng nearRight,
                                                    MapLatLng farLeft,
                                                    MapLatLng farRight,
                                                    MapLatLngBounds latLngBounds) {
        return new MapVisibleRegion(nearLeft, nearRight, farLeft, farRight, latLngBounds);
    }


    public MapLatLngBounds convertMapLatLngBounds(MapLatLng southwest, MapLatLng northeast, MapLatLng center) {
        return new MapLatLngBounds(southwest, northeast, center);
    }

    public MapMarker convertMapMarker(MapLatLng mapLatLng, String title) {
        return new MapMarker(mapLatLng, title);
    }

}

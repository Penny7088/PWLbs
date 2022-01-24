package com.pw.lib.lbs.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * com.hellotalk.lib.map.entity
 *
 * @author : Penny (penny@hellotalk.com)
 * @describe :
 * @date : 4/7/21
 */
public class MapLatLngBounds implements Parcelable {

    public MapLatLng southwest;

    public MapLatLng northeast;

    public MapLatLng center;

    public MapLatLngBounds() {
    }

    public MapLatLngBounds(MapLatLng southwest, MapLatLng northeast, MapLatLng center) {
        this.southwest = southwest;
        this.northeast = northeast;
        this.center = center;
    }

    protected MapLatLngBounds(Parcel in) {
        southwest = in.readParcelable(MapLatLng.class.getClassLoader());
        northeast = in.readParcelable(MapLatLng.class.getClassLoader());
        center = in.readParcelable(MapLatLng.class.getClassLoader());
    }

    public static final Creator<MapLatLngBounds> CREATOR = new Creator<MapLatLngBounds>() {
        @Override
        public MapLatLngBounds createFromParcel(Parcel in) {
            return new MapLatLngBounds(in);
        }

        @Override
        public MapLatLngBounds[] newArray(int size) {
            return new MapLatLngBounds[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(southwest, flags);
        dest.writeParcelable(northeast, flags);
        dest.writeParcelable(center, flags);
    }

    @Override
    public String toString() {
        return "MapLatLngBounds{" +
                "southwest=" + southwest +
                ", northeast=" + northeast +
                ", center=" + center +
                '}';
    }
}

package com.pw.lib.lbs.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * com.hellotalk.lib.map.entity
 *
 * @author : Penny (penny@hellotalk.com)
 * @describe :
 * @date : 4/6/21
 */
public class MapLatLng implements Parcelable {

    private double longitude;
    private double latitude;

    public MapLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected MapLatLng(Parcel in) {
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<MapLatLng> CREATOR = new Creator<MapLatLng>() {
        @Override
        public MapLatLng createFromParcel(Parcel in) {
            return new MapLatLng(in);
        }

        @Override
        public MapLatLng[] newArray(int size) {
            return new MapLatLng[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    @Override
    public String toString() {
        return "MapLatLng{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapLatLng)) return false;
        MapLatLng mapLatLng = (MapLatLng) o;
        return Double.compare(mapLatLng.getLongitude(), getLongitude()) == 0 &&
                Double.compare(mapLatLng.getLatitude(), getLatitude()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLongitude(), getLatitude());
    }
}

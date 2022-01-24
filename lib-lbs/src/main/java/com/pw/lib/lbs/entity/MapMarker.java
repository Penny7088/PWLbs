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
public class MapMarker implements Parcelable {

    private MapLatLng latLng;

    private String title;

    public MapMarker(MapLatLng latLng, String title) {
        this.latLng = latLng;
        this.title = title;
    }


    protected MapMarker(Parcel in) {
        latLng = in.readParcelable(MapLatLng.class.getClassLoader());
        title = in.readString();
    }

    public static final Creator<MapMarker> CREATOR = new Creator<MapMarker>() {
        @Override
        public MapMarker createFromParcel(Parcel in) {
            return new MapMarker(in);
        }

        @Override
        public MapMarker[] newArray(int size) {
            return new MapMarker[size];
        }
    };

    public MapLatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(MapLatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

    @Override
    public String toString() {
        return "MapMarker{" +
                "latLng=" + latLng +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapMarker)) return false;
        MapMarker mapMarker = (MapMarker) o;
        return getLatLng().equals(mapMarker.getLatLng()) &&
                getTitle().equals(mapMarker.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatLng(), getTitle());
    }
}

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
public class MapVisibleRegion implements Parcelable {

    public MapLatLng nearLeft;

    public MapLatLng nearRight;

    public MapLatLng farLeft;

    public MapLatLng farRight;

    public MapLatLngBounds latLngBounds;

    public MapVisibleRegion() {
    }

    public MapVisibleRegion(MapLatLng nearLeft, MapLatLng nearRight, MapLatLng farLeft, MapLatLng farRight, MapLatLngBounds latLngBounds) {
        this.nearLeft = nearLeft;
        this.nearRight = nearRight;
        this.farLeft = farLeft;
        this.farRight = farRight;
        this.latLngBounds = latLngBounds;
    }

    public MapLatLng getNearLeft() {
        return nearLeft;
    }

    public void setNearLeft(MapLatLng nearLeft) {
        this.nearLeft = nearLeft;
    }

    public MapLatLng getNearRight() {
        return nearRight;
    }

    public void setNearRight(MapLatLng nearRight) {
        this.nearRight = nearRight;
    }

    public MapLatLng getFarLeft() {
        return farLeft;
    }

    public void setFarLeft(MapLatLng farLeft) {
        this.farLeft = farLeft;
    }

    public MapLatLng getFarRight() {
        return farRight;
    }

    public void setFarRight(MapLatLng farRight) {
        this.farRight = farRight;
    }

    public MapLatLngBounds getLatLngBounds() {
        return latLngBounds;
    }

    public void setLatLngBounds(MapLatLngBounds latLngBounds) {
        this.latLngBounds = latLngBounds;
    }

    public static Creator<MapVisibleRegion> getCREATOR() {
        return CREATOR;
    }

    protected MapVisibleRegion(Parcel in) {
        nearLeft = in.readParcelable(MapLatLng.class.getClassLoader());
        nearRight = in.readParcelable(MapLatLng.class.getClassLoader());
        farLeft = in.readParcelable(MapLatLng.class.getClassLoader());
        farRight = in.readParcelable(MapLatLng.class.getClassLoader());
        latLngBounds = in.readParcelable(MapLatLngBounds.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(nearLeft, flags);
        dest.writeParcelable(nearRight, flags);
        dest.writeParcelable(farLeft, flags);
        dest.writeParcelable(farRight, flags);
        dest.writeParcelable(latLngBounds, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MapVisibleRegion> CREATOR = new Creator<MapVisibleRegion>() {
        @Override
        public MapVisibleRegion createFromParcel(Parcel in) {
            return new MapVisibleRegion(in);
        }

        @Override
        public MapVisibleRegion[] newArray(int size) {
            return new MapVisibleRegion[size];
        }
    };

    @Override
    public String toString() {
        return "MapVisibleRegion{" +
                "nearLeft=" + nearLeft +
                ", nearRight=" + nearRight +
                ", farLeft=" + farLeft +
                ", farRight=" + farRight +
                ", latLngBounds=" + latLngBounds +
                '}';
    }
}

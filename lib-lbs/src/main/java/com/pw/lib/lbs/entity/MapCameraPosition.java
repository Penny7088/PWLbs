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
public class MapCameraPosition implements Parcelable {

    public MapLatLng target;
    public float zoom;
    public float tilt;
    public float bearing;

    public MapCameraPosition(MapLatLng target, float zoom, float tilt, float bearing) {
        this.target = target;
        this.zoom = zoom;
        this.tilt = tilt;
        this.bearing = bearing;
    }


    protected MapCameraPosition(Parcel in) {
        target = in.readParcelable(MapLatLng.class.getClassLoader());
        zoom = in.readFloat();
        tilt = in.readFloat();
        bearing = in.readFloat();
    }

    public static final Creator<MapCameraPosition> CREATOR = new Creator<MapCameraPosition>() {
        @Override
        public MapCameraPosition createFromParcel(Parcel in) {
            return new MapCameraPosition(in);
        }

        @Override
        public MapCameraPosition[] newArray(int size) {
            return new MapCameraPosition[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(target, flags);
        dest.writeFloat(zoom);
        dest.writeFloat(tilt);
        dest.writeFloat(bearing);
    }

    @Override
    public String toString() {
        return "MapCameraPosition{" +
                "target=" + target +
                ", zoom=" + zoom +
                ", tilt=" + tilt +
                ", bearing=" + bearing +
                '}';
    }
}

package com.pw.lib.amap;

import android.content.Context;
import android.content.res.Resources;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.VisibleRegion;
import com.pw.lib.lbs.entity.MapCameraPosition;
import com.pw.lib.lbs.entity.MapLatLng;
import com.pw.lib.lbs.entity.MapMarker;
import com.pw.lib.lbs.entity.MapVisibleRegion;
import com.pw.lib.lbs.log.LoggerUtils;
import com.pw.lib.lbs.map.IBaseMapView;
import com.pw.lib.lbs.map.OnCameraIdleListener;
import com.pw.lib.lbs.map.OnCameraMoveListener;
import com.pw.lib.lbs.map.OnMapClickListener;
import com.pw.lib.lbs.map.OnMapReadyCallback;
import com.pw.lib.lbs.map.OnMarkerClickListener;
import com.pw.lib.lbs.util.ConvertHelper;

import java.util.List;

/**
 * com.pw.lib.amap
 *
 * @author : Penny (penny@pw.com)
 * @describe : 高德地图实现类
 * @date : 4/2/21
 */
public class AMapViewImpl implements IBaseMapView, OnMapLoadedListener {

    private static final String TAG = "AMapViewImpl";

    private ConvertHelper mConvertHelper;

    private AMapSupportFragment mMapFragment;

    private AMap mAMap;

    private UiSettings mUiSettings;
    /*上一次点击的 marker*/
    private Marker mLastSelectedMarker;

    private AMapCustomMarker mCustomMarker;
    private OnMapReadyCallback readyCallback;

    public AMapViewImpl() {
        mConvertHelper = new ConvertHelper();
    }

    @Override
    public IBaseMapView createMapFragment(boolean isTransmit) {
        if (isTransmit) {
            AMapOptions aMapOptions = new AMapOptions();
        }
        mMapFragment = AMapSupportFragment.newInstance();
        mMapFragment.setOnMapLoadedListener(this);
        return this;
    }

    @Override
    public Fragment obtainMapFragment() {
        if (mMapFragment != null) {
            return mMapFragment;
        }
        return null;
    }

    @Override
    public void hideFragment(FragmentTransaction transaction) {
        if (transaction != null) {
            transaction.hide(mMapFragment);
        }
    }

    @Override
    public void getMapAsync(OnMapReadyCallback listener) {
        readyCallback = listener;
    }

    @Override
    public void setOnCameraIdleListener(OnCameraIdleListener listener) {
        if (!checkReady() && listener != null) {
            LoggerUtils.INSTANCE.i(TAG, "setOnCameraIdleListener >>> map fragment or listener is null");
            return;
        }
        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                LatLng target = cameraPosition.target;
                float zoom = cameraPosition.zoom;
                float tilt = cameraPosition.tilt;
                VisibleRegion visibleRegion = mAMap.getProjection().getVisibleRegion();

                MapCameraPosition mapCameraPosition = mConvertHelper.convertMapCameraPosition(target.latitude,
                        target.longitude, zoom, tilt, cameraPosition.bearing);

                MapVisibleRegion mapVisibleRegion = mConvertHelper.convertMapVisibleRegion(
                        mConvertHelper.convertMapLatLng(visibleRegion.nearLeft.latitude, visibleRegion.nearLeft.longitude),
                        mConvertHelper.convertMapLatLng(visibleRegion.nearRight.latitude, visibleRegion.nearRight.longitude),
                        mConvertHelper.convertMapLatLng(visibleRegion.farLeft.latitude, visibleRegion.farLeft.longitude),
                        mConvertHelper.convertMapLatLng(visibleRegion.farRight.latitude, visibleRegion.farRight.longitude),
                        mConvertHelper.convertMapLatLngBounds(
                                mConvertHelper.convertMapLatLng(visibleRegion.latLngBounds.southwest.latitude, visibleRegion.latLngBounds.southwest.longitude),
                                mConvertHelper.convertMapLatLng(visibleRegion.latLngBounds.northeast.latitude, visibleRegion.latLngBounds.northeast.longitude),
                                mConvertHelper.convertMapLatLng(target.latitude, target.longitude)
                        )
                );

                listener.onCameraIdle(mapCameraPosition, mapVisibleRegion);

            }
        });

    }

    @Override
    public void setOnMapClickListener(OnMapClickListener listener) {

    }

    @Override
    public void setOnMarkerClickListener(OnMarkerClickListener listener) {
        if (!checkReady() && listener != null) {
            LoggerUtils.INSTANCE.i(TAG, "setOnMarkerClickListener >>> map fragment or listener is null");
            return;
        }

        mAMap.setOnMarkerClickListener(marker -> {
            LoggerUtils.INSTANCE.i(TAG, "onMarkerClick>>> marker%s" + marker.toString());
            float zIndex = marker.getZIndex();
            marker.setZIndex(zIndex + 1.0f);
            listener.onMarkerClick(mConvertHelper.convertMapMarker(
                    mConvertHelper.convertMapLatLng(marker.getPosition().latitude, marker.getPosition().longitude),
                    marker.getTitle()), mLastSelectedMarker != null && mLastSelectedMarker.equals(marker)
            );

            mLastSelectedMarker = marker;


            return false;
        });
    }

    @Override
    public void setOnCameraMoveListener(OnCameraMoveListener listener) {

    }

    @Override
    public List<MapMarker> obtainMarkers() {
        return null;
    }

    @Override
    public void removeMarker(MapMarker marker) {

    }

    @Override
    public void removeMarkers(List<MapMarker> marker) {

    }

    @Override
    public void setMapType(int type) {

    }

    @Override
    public void defaultSetUp() {
        if (!checkReady()) {
            LoggerUtils.INSTANCE.i(TAG, "map fragment or mapView is null");
            return;
        }
        mUiSettings = mAMap.getUiSettings();
        mUiSettings.setScaleControlsEnabled(false);
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setMyLocationButtonEnabled(false);
        mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        mUiSettings.setRotateGesturesEnabled(false);//禁止地图旋转手势.
        mUiSettings.setTiltGesturesEnabled(false);
    }

    @Override
    public void addCustomMarkers(List<MapMarker> markers) {
        LoggerUtils.INSTANCE.i(TAG, "addCustomMarkers >>> ");
        if (markers == null && markers.size() == 0) {
            LoggerUtils.INSTANCE.i(TAG, "addCustomMarkers >>> markers is null");
            return;
        }

        if (!checkReady()) {
            LoggerUtils.INSTANCE.i(TAG, "addCustomMarkers >>> mFragment is null or aMap is null");
            return;
        }

        for (MapMarker marker : markers) {
            addMarker(marker);
        }
    }

    @Override
    public void addMarker(MapMarker marker) {
        LoggerUtils.INSTANCE.i(TAG, "addMarker >>>  ");
        if (!checkReady()) {
            LoggerUtils.INSTANCE.i(TAG, "addMarker >>>  googleMap is null");
            return;
        }

        if (mCustomMarker == null) {
            mCustomMarker = new AMapCustomMarker(mMapFragment.getContext());
        }

        BitmapDescriptor customView = mCustomMarker.generateCustomMarker(marker);
        MapLatLng latLng = marker.getLatLng();
        LatLng lng = new LatLng(latLng.getLatitude(), latLng.getLongitude());
        mAMap.addMarker(new MarkerOptions()
                .position(lng)
                .icon(customView)
                .visible(true));

    }

    @Override
    public void addIconMarker(MapMarker marker, Resources resource, int iconRes) {

    }

    @Override
    public void clearMarker() {
        if (!checkReady()) {
            LoggerUtils.INSTANCE.i(TAG, "setMinMaxZoom >>>  aMap is null");
            return;
        }
        mAMap.clear();
    }

    @Override
    public void moveCamera(MapLatLng latLng) {
        if (!checkReady()) {
            LoggerUtils.INSTANCE.i(TAG, "setMinMaxZoom >>>  aMap is null");
            return;
        }
        LatLng lng = new LatLng(latLng.getLatitude(), latLng.getLongitude());
        CameraPosition position = new CameraPosition(lng, 8, 0, 0);
        mAMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));

    }

    @Override
    public void moveCameraBounds(List<MapLatLng> mapLatLng) {

    }

    @Override
    public void setScrollGesturesEnabled(boolean enabled) {

    }

    @Override
    public void setMinMaxZoom(float min, float max) {
        if (!checkReady()) {
            LoggerUtils.INSTANCE.i(TAG, "setMinMaxZoom >>>  aMap is null");
            return;
        }

        mAMap.setMinZoomLevel(min);
        mAMap.setMaxZoomLevel(max);

    }

    @Override
    public void setBestZoom() {
        setMinMaxZoom(2f, 8f);
    }

    @Override
    public void setMyLocationEnable(Context context, boolean enabled) {

    }

    private boolean checkReady() {
        return mMapFragment != null && mAMap != null;
    }

    @Override
    public void onMapLoaded() {
        LoggerUtils.INSTANCE.i(TAG, "onMapLoaded >>>");
        mAMap = mMapFragment.getMap();
        defaultSetUp();
        if (readyCallback != null) {
            readyCallback.onMapReady();
        }

    }
}

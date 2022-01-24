package com.pw.lib.lbs.google

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.pw.lib.lbs.entity.MapLatLng
import com.pw.lib.lbs.entity.MapMarker
import com.pw.lib.lbs.log.LoggerUtils.d
import com.pw.lib.lbs.log.LoggerUtils.w
import com.pw.lib.lbs.map.*
import com.pw.lib.lbs.map.OnMapReadyCallback
import com.pw.lib.lbs.util.ConvertHelper
import java.util.*

/**
 * com.hellotalk.lib.gmap
 *
 * @author : Penny (penny@hellotalk.com)
 * @describe : google map 实现类
 * @date : 4/2/21
 */
class GMapViewImpl : IBaseMapView {
    private val mConvertHelper: ConvertHelper

    /**
     * 地图fragment
     */
    private var mFragment: SupportMapFragment? = null

    /**
     * 地图 mapView
     */
    private var googleMap: GoogleMap? = null

    /**
     * 填充类
     */
    private val mInflater: LayoutInflater? = null

    /**
     * 自定义marker 辅助类
     */
    private var mCustomMarker: GMapCustomMarker? = null

    /*上一次点击的marker*/
    private var mLastSelectedMarker: Marker? = null

    /**
     * ui 设置
     */
    private var mUiSettings: UiSettings? = null

    /**
     * 包装后的标记
     */
    private var mMarkers: MutableList<MapMarker>? = null

    /**
     * 原生的集合标记
     */
    private var mNativeMarkers: ArrayList<Marker>? = null
    override fun createMapFragment(isTransmit: Boolean): IBaseMapView {
        if (isTransmit) {
            val options = GoogleMapOptions()
        }
        mFragment = SupportMapFragment.newInstance()
        return this
    }

    override fun obtainMapFragment(): Fragment? {
        return mFragment
    }

    override fun hideFragment(transaction: FragmentTransaction) {
        transaction.hide(mFragment!!)
    }

    @SuppressLint("MissingPermission")
    override fun getMapAsync(listener: OnMapReadyCallback) {
        mFragment?.getMapAsync { googleMap: GoogleMap? ->
            this.googleMap = googleMap
//                defaultSetUp()
            googleMap?.isMyLocationEnabled = false
            listener.onMapReady()
        }
    }

    override fun setOnCameraIdleListener(listener: OnCameraIdleListener) {
        d(TAG, "setOnCameraIdleListener >>>")
        if (listener != null && checkReady()) {
            d(TAG, "setOnCameraIdleListener")
            googleMap!!.setOnCameraIdleListener {
                val position = googleMap!!.cameraPosition
                val target = position.target
                val zoom = position.zoom
                val tilt = position.tilt
                val visibleRegion = googleMap!!.projection.visibleRegion
                val mapCameraPosition = mConvertHelper.convertMapCameraPosition(
                    target.latitude,
                    target.longitude, zoom, tilt, position.bearing
                )
                val mapVisibleRegion = mConvertHelper.convertMapVisibleRegion(
                    mConvertHelper.convertMapLatLng(
                        visibleRegion.nearLeft.latitude,
                        visibleRegion.nearLeft.longitude
                    ),
                    mConvertHelper.convertMapLatLng(
                        visibleRegion.nearRight.latitude,
                        visibleRegion.nearRight.longitude
                    ),
                    mConvertHelper.convertMapLatLng(
                        visibleRegion.farLeft.latitude,
                        visibleRegion.farLeft.longitude
                    ),
                    mConvertHelper.convertMapLatLng(
                        visibleRegion.farRight.latitude,
                        visibleRegion.farRight.longitude
                    ),
                    mConvertHelper.convertMapLatLngBounds(
                        mConvertHelper.convertMapLatLng(
                            visibleRegion.latLngBounds.southwest.latitude,
                            visibleRegion.latLngBounds.southwest.longitude
                        ),
                        mConvertHelper.convertMapLatLng(
                            visibleRegion.latLngBounds.northeast.latitude,
                            visibleRegion.latLngBounds.northeast.longitude
                        ),
                        mConvertHelper.convertMapLatLng(
                            visibleRegion.latLngBounds.center.latitude,
                            visibleRegion.latLngBounds.center.longitude
                        )
                    )
                )
                listener.onCameraIdle(mapCameraPosition, mapVisibleRegion)
            }
        } else {
            w(TAG, "google map is null or listener is null")
        }
    }

    override fun setOnMapClickListener(listener: OnMapClickListener) {
        if (!checkReady()) {
            w(TAG, "setOnMapClickListener >>>> gmap is null or listener is null")
            return
        }
        googleMap!!.setOnMapClickListener { latLng: LatLng ->
            d(TAG, "latlng:$latLng")
            listener.onMapClick(mConvertHelper.convertMapLatLng(latLng.latitude, latLng.longitude))
        }
    }

    override fun setOnMarkerClickListener(listener: OnMarkerClickListener) {
        if (!checkReady()) {
            w(TAG, "setOnMarkerClickListener >>>> gmap is null or listener is null")
            return
        }
        googleMap!!.setOnMarkerClickListener { marker: Marker ->
            d(TAG, "onMarkerClick>>> marker:$marker")
            val zIndex = marker.zIndex
            marker.zIndex = zIndex + 1.0f
            val title = marker.title
            val marker2 = mConvertHelper.convertMapMarker(
                mConvertHelper.convertMapLatLng(
                    marker.position.latitude,
                    marker.position.longitude
                ),
                title
            )
            listener.onMarkerClick(
                marker2, mLastSelectedMarker != null && mLastSelectedMarker == marker
            )
            mLastSelectedMarker = marker
            if (TextUtils.isEmpty(title)) {
                return@setOnMarkerClickListener false
            }
            removeMarkers(mMarkers!!)
            addCustomMarkers(mMarkers!!)
            if (mCustomMarker == null) {
                mCustomMarker = GMapCustomMarker(mFragment!!.context)
            }
            if (mNativeMarkers == null) {
                mNativeMarkers = ArrayList()
            }
            val customView = mCustomMarker!!.generateCustomMarkerBlue(marker2)
            val latLng = marker2.latLng
            val lng = LatLng(latLng.latitude, latLng.longitude)
            val marker1 = googleMap!!.addMarker(
                MarkerOptions()
                    .position(lng)
                    .icon(customView)
                    .title(marker.title)
                    .visible(true)
            )
            marker1.tag = marker2
            mNativeMarkers!!.add(marker1)
            marker.remove()
            false
        }
    }

    override fun setOnCameraMoveListener(listener: OnCameraMoveListener) {}

    override fun obtainMarkers(): List<MapMarker>? {
        d(TAG, "mMarkers:$mMarkers")
        return mMarkers
    }

    override fun removeMarker(marker: MapMarker) {
        w(TAG, "removeMarker >>>  ")
        if (marker == null) {
            w(TAG, "removeMarker >>>  marker is null")
            return
        }
        if (!checkReady()) {
            w(TAG, "removeMarker >>>  googleMap is null")
            return
        }
        if (mNativeMarkers != null && mNativeMarkers!!.size > 0) {
            for (lat in mNativeMarkers!!) {
                val tag = lat.tag
                if (tag != null && tag is MapMarker) {
                    val markerTag = tag
                    w(TAG, "removeMarker:$markerTag")
                    if (markerTag == marker) {
                        lat.remove()
                    }
                }
            }
        }
    }

    override fun removeMarkers(markers: List<MapMarker>) {
        w(TAG, "removeMarkers >>> ")
        if (markers == null && markers.size == 0) {
            w(TAG, "removeMarkers >>> markers is null")
            return
        }
        if (!checkReady() || mFragment == null) {
            w(TAG, "addCustomMarkers >>> mFragment is null or googleMap is null")
            return
        }
        for (marker in markers) {
            removeMarker(marker)
        }
    }

    override fun setMapType(type: Int) {
        googleMap?.mapType = type
    }

    override fun defaultSetUp() {
        if (checkReady()) {
            mUiSettings = googleMap?.uiSettings
            mUiSettings?.isZoomControlsEnabled = false
            mUiSettings?.isCompassEnabled = false
            mUiSettings?.isRotateGesturesEnabled = false
            mUiSettings?.isMapToolbarEnabled = false
            setMinMaxZoom(2f, 11f)
        } else {
            w(TAG, "google map is null")
        }
    }

    override fun addCustomMarkers(markers: MutableList<MapMarker>) {
        w(TAG, "addCustomMarkers >>> ")
        if (markers == null && markers.size == 0) {
            w(TAG, "addCustomMarkers >>> markers is null")
            return
        }
        if (!checkReady() || mFragment == null) {
            w(TAG, "addCustomMarkers >>> mFragment is null or googleMap is null")
            return
        }
        mMarkers = markers
        for (marker in markers) {
            addMarker(marker)
        }
    }

    override fun addMarker(marker: MapMarker) {
        w(TAG, "addMarker >>>  ")
        if (!checkReady()) {
            w(TAG, "addMarker >>>  googleMap is null")
            return
        }
        if (mCustomMarker == null) {
            mCustomMarker = GMapCustomMarker(mFragment!!.context)
        }
        if (mNativeMarkers == null) {
            mNativeMarkers = ArrayList()
        }
        val customView = mCustomMarker!!.generateCustomMarker(marker)
        val latLng = marker.latLng
        val lng = LatLng(latLng.latitude, latLng.longitude)
        val marker1 = googleMap!!.addMarker(
            MarkerOptions()
                .position(lng)
                .icon(customView)
                .title(marker.title)
                .visible(true)
        )
        marker1.tag = marker
        mNativeMarkers!!.add(marker1)
    }

    override fun addIconMarker(marker: MapMarker?, resource: Resources?, iconRes: Int) {
        w(TAG, "addIconMarker >>>  ")
        if (!checkReady()) {
            w(TAG, "addIconMarker >>>  googleMap is null")
            return
        }
        if (mCustomMarker == null) {
            mCustomMarker = GMapCustomMarker(mFragment!!.context)
        }
        if (mNativeMarkers == null) {
            mNativeMarkers = ArrayList()
        }

        marker?.let {
            val bitmap = BitmapFactory.decodeResource(resource, iconRes)
            val bitmapDescriptor: BitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)
            bitmap.recycle()
            val latLng = marker.latLng
            val lng = LatLng(latLng.latitude, latLng.longitude)
            val marker1 = googleMap!!.addMarker(
                MarkerOptions()
                    .position(lng)
                    .icon(bitmapDescriptor)
                    .title(marker.title)
                    .visible(true)
            )
            marker1.tag = marker
            mNativeMarkers!!.add(marker1)
        }
    }

    override fun clearMarker() {
        if (!checkReady()) {
            w(TAG, "clearMarker >>>  googleMap is null")
            return
        }
        googleMap!!.clear()
        if (mNativeMarkers != null) {
            mNativeMarkers!!.clear()
        }
        if (mMarkers != null) {
            mMarkers!!.clear()
        }
        mNativeMarkers = null
        mMarkers = null
    }

    override fun moveCamera(mapLatLng: MapLatLng) {
        if (!checkReady()) {
            w(TAG, "moveCamera >>> googleMap is null")
            return
        }
        val lng = LatLng(mapLatLng.latitude, mapLatLng.longitude)
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 13f))
    }

    override fun moveCameraBounds(mapLatLng: List<MapLatLng>) {}
    override fun setScrollGesturesEnabled(enabled: Boolean) {
        if (!checkReady() || mUiSettings == null) {
            w(TAG, "clearMarker >>>  googleMap is null")
            return
        }
        mUiSettings!!.isScrollGesturesEnabled = enabled
    }

    override fun setMinMaxZoom(min: Float, max: Float) {
        if (!checkReady()) {
            w(TAG, "setMinMaxZoom >>>  googleMap is null")
            return
        }
        googleMap!!.setMaxZoomPreference(max)
        googleMap!!.setMinZoomPreference(min)
    }

    override fun setBestZoom() {
        mUiSettings = googleMap?.uiSettings
        mUiSettings?.isZoomControlsEnabled = false
        mUiSettings?.isCompassEnabled = false
        mUiSettings?.isRotateGesturesEnabled = false
        mUiSettings?.isMapToolbarEnabled = false
        googleMap?.setMinZoomPreference(10f)
    }

    override fun setMyLocationEnable(context: Context, enabled: Boolean) {
        if (checkReady() && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap!!.isMyLocationEnabled = enabled
        }
    }

    private fun checkReady(): Boolean {
        return googleMap != null
    }

    companion object {
        private const val TAG = "GMapViewImpl"
    }

    init {
        mConvertHelper = ConvertHelper()
    }
}
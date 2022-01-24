package com.pw.lib.lbs.baidu

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.model.LatLngBounds
import com.pw.lib.lbs.entity.*
import com.pw.lib.lbs.log.LoggerUtils
import com.pw.lib.lbs.map.*
import com.pw.lib.lbs.util.ConvertHelper
import java.util.*


/**
 *  百度地图实现类
 *
 *  create by Huangzefeng on 1/9/2021
 */
class BaiduMapViewImpl : IBaseMapView, OnMapLoadCallback,
    LifecycleObserver {

    companion object {
        const val TAG = "BaiduMapViewImpl"
    }

    private val mConvertHelper: ConvertHelper
    private var mBaiduMapFragment: BaiduMapSupportFragment? = null
    private var mMap: BaiduMap? = null
    private var mUiSettings: UiSettings? = null
    private var mapReadyCallback: OnMapReadyCallback? = null

    private var mMarkers: MutableList<MapMarker>? = null
    private var mCustomMarker: BaiduMapCustomMaker? = null

    /*上一次点击的 marker*/
    private var mLastSelectedMarker: Marker? = null

    /**
     * 原生的集合标记
     */
    private var mNativeMarkers: ArrayList<Overlay>? = null
    private var initData: Boolean = false

    init {
        mConvertHelper = ConvertHelper()
    }

    override fun createMapFragment(isTransmit: Boolean): IBaseMapView {
        LoggerUtils.w(TAG, "createMapFragment() ")
        initData = false
        mBaiduMapFragment = BaiduMapSupportFragment.newInstance()
        mBaiduMapFragment?.onMapLoadedCallback = this
        mBaiduMapFragment?.registerLifecycleObserver(this)
        return this
    }

    override fun obtainMapFragment(): Fragment? {
        return mBaiduMapFragment
    }

    override fun hideFragment(transaction: FragmentTransaction?) {
        mBaiduMapFragment?.let {
            transaction?.hide(it)
        }
    }

    override fun defaultSetUp() {
        if (!checkReady()) {
            LoggerUtils.d(TAG, "map fragment or mapView is null")
            return
        }
        mUiSettings = mMap?.uiSettings
        mUiSettings?.isZoomGesturesEnabled = true
        mUiSettings?.isCompassEnabled = false
        mUiSettings?.isRotateGesturesEnabled = false //禁止地图旋转手势.
        mMap?.isMyLocationEnabled = false
        setMinMaxZoom(4f, 12.5f)
    }

    override fun addCustomMarkers(markers: MutableList<MapMarker>?) {
        LoggerUtils.w(TAG, "addCustomMarkers >>> ")
        if (markers?.size ?: 0 == 0) {
            LoggerUtils.w(TAG, "addCustomMarkers >>> markers is null")
            return
        }

        if (!checkReady()) {
            LoggerUtils.w(TAG, "addCustomMarkers >>> mFragment is null or aMap is null")
            return
        }

        mMarkers = markers

        for (marker in markers!!) {
            addMarker(marker)
        }
    }

    override fun addIconMarker(marker: MapMarker?, resource: Resources, iconRes: Int) {
        LoggerUtils.w(TAG, "addIconMarker >>>  ")
        if (!checkReady()) {
            LoggerUtils.w(TAG, "addIconMarker >>>  baiduMap is null")
            return
        }

        if (mNativeMarkers == null) {
            mNativeMarkers = ArrayList()
        }

        val bitmap = BitmapFactory.decodeResource(resource, iconRes)
        val bitmapDescriptor: BitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)
        bitmap.recycle()
        val latLng = marker!!.latLng
        val lng = LatLng(latLng.latitude, latLng.longitude)
        val marker1: Overlay? =
            mMap?.addOverlay(
                MarkerOptions().position(lng).icon(bitmapDescriptor).title(marker.title)
                    .visible(true)
            )
        if (marker1 != null) {
            val info = Bundle()
            info.putDouble("lat", latLng.latitude)
            info.putDouble("lng", latLng.longitude)
            marker1.extraInfo = info
            mNativeMarkers?.add(marker1)
        }
    }

    override fun addMarker(marker: MapMarker?) {
        LoggerUtils.w(TAG, "addMarker >>>  ")
        if (!checkReady()) {
            LoggerUtils.w(TAG, "addMarker >>>  baiduMap is null")
            return
        }

        if (mCustomMarker == null) {
            mCustomMarker = BaiduMapCustomMaker(mBaiduMapFragment!!.requireContext())
        }

        if (mNativeMarkers == null) {
            mNativeMarkers = ArrayList()
        }

        val customView: BitmapDescriptor? = mCustomMarker?.generateCustomMarker(marker)
        val latLng = marker!!.latLng
        val lng = LatLng(latLng.latitude, latLng.longitude)
        val marker1: Overlay? =
            mMap?.addOverlay(
                MarkerOptions().position(lng).icon(customView).title(marker.title).visible(true)
            )
        if (marker1 != null) {
            val info = Bundle()
            info.putDouble("lat", latLng.latitude)
            info.putDouble("lng", latLng.longitude)
            marker1.extraInfo = info
            mNativeMarkers?.add(marker1)
        }

    }

    override fun clearMarker() {
        if (!checkReady()) {
            LoggerUtils.w(TAG, "setMinMaxZoom >>>  aMap is null")
            return
        }
        mMap?.clear()
        if (mNativeMarkers != null) {
            mNativeMarkers!!.clear()
        }
        if (mMarkers != null) {
            mMarkers?.clear()
        }
        mNativeMarkers = null
        mMarkers = null
    }

    override fun getMapAsync(listener: OnMapReadyCallback?) {
        mapReadyCallback = listener
    }

    override fun moveCamera(latLng: MapLatLng?) {
        if (!checkReady()) {
            LoggerUtils.w(TAG, "setMinMaxZoom >>>  aMap is null")
            return
        }
        latLng?.let {
            val status = MapStatusUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude))
            mMap?.setMapStatus(status)
            initData = false
        }
    }

    override fun moveCameraBounds(mapLatLng: MutableList<MapLatLng>?) {
    }

    override fun setScrollGesturesEnabled(enabled: Boolean) {
    }

    override fun setMinMaxZoom(min: Float, max: Float) {
        if (!checkReady()) {
            LoggerUtils.w(TAG, "setMinMaxZoom >>>  aMap is null")
            return
        }

        mMap?.setMaxAndMinZoomLevel(max, min)
        mMap?.clear()
    }

    override fun setBestZoom() {
        setMinMaxZoom(1f, 5f)
    }

    override fun setMyLocationEnable(context: Context?, enabled: Boolean) {
        mMap?.isMyLocationEnabled = enabled
    }

    override fun setOnCameraIdleListener(listener: OnCameraIdleListener?) {
        if (!checkReady() && listener != null) {
            LoggerUtils.w(TAG, "setOnCameraIdleListener >>> map fragment or listener is null")
            return
        }

        mMap?.setOnMapStatusChangeListener(object : BaiduMap.OnMapStatusChangeListener {
            override fun onMapStatusChangeStart(p0: MapStatus?) {
                LoggerUtils.w(TAG, "onMapStatusChangeStart >>> ")
            }

            override fun onMapStatusChangeStart(p0: MapStatus?, p1: Int) {
                LoggerUtils.w(TAG, "onMapStatusChangeStart >>> two  ")
            }

            override fun onMapStatusChange(status: MapStatus?) {
                LoggerUtils.w(TAG, "onMapStatusChange >>> ")
                if (!initData) {
                    initData = true
                    status?.let {
                        onCameraChanged(
                            it.target.latitude,
                            it.target.longitude,
                            it.zoom,
                            0f,
                            it.bound,
                            listener
                        )
                    }
                }
            }

            override fun onMapStatusChangeFinish(status: MapStatus?) {
                LoggerUtils.w(TAG, "onMapStatusChangeFinish >>> ")
                status?.let {
                    onCameraChanged(
                        it.target.latitude,
                        it.target.longitude,
                        it.zoom,
                        0f,
                        it.bound,
                        listener
                    )
                }
            }

        })
//        mMap?.setOnMarkerDragListener(object : BaiduMap.OnMarkerDragListener {
//            override fun onMarkerDrag(marker: Marker?) {
//                marker?.let {
//                    val zoom: Float = it.scale
//                    val tilt = 0f
//                    val bound = mMap?.mapStatus?.bound
//                    onCameraChanged(
//                        it.position.latitude,
//                        it.position.longitude,
//                        zoom,
//                        tilt,
//                        bound,
//                        listener
//                    )
//                }
//            }
//
//            override fun onMarkerDragEnd(p0: Marker?) {
//            }
//
//            override fun onMarkerDragStart(p0: Marker?) {
//            }
//
//        })
    }

    private fun onCameraChanged(
        latitude: Double,
        longitude: Double,
        zoom: Float,
        tilt: Float,
        bound: LatLngBounds?,
        listener: OnCameraIdleListener?
    ) {
        val mapCameraPosition: MapCameraPosition =
            mConvertHelper.convertMapCameraPosition(
                latitude, longitude, zoom, tilt, 0f
            )
        val southWest = mConvertHelper.convertMapLatLng(
            bound?.southwest?.latitude ?: 0.0,
            bound?.southwest?.longitude ?: 0.0
        )
        val northEast = mConvertHelper.convertMapLatLng(
            bound?.northeast?.latitude ?: 0.0,
            bound?.northeast?.longitude ?: 0.0
        )
        val center = mConvertHelper.convertMapLatLng(
            mMap?.locationData?.latitude ?: 0.0,
            mMap?.locationData?.longitude ?: 0.0
        )
        val mapVisibleBounds: MapLatLngBounds =
            mConvertHelper.convertMapLatLngBounds(southWest, northEast, center)
        val mapVisibleRegion: MapVisibleRegion = mConvertHelper.convertMapVisibleRegion(
            mConvertHelper.convertMapLatLng(
                bound?.southwest?.latitude ?: 0.0,
                bound?.southwest?.longitude ?: 0.0
            ),
            mConvertHelper.convertMapLatLng(
                bound?.northeast?.latitude ?: 0.0,
                bound?.southwest?.longitude ?: 0.0
            ),
            mConvertHelper.convertMapLatLng(
                bound?.southwest?.latitude ?: 0.0,
                bound?.northeast?.longitude ?: 0.0
            ),
            mConvertHelper.convertMapLatLng(
                bound?.northeast?.latitude ?: 0.0,
                bound?.northeast?.longitude ?: 0.0
            ),
            mapVisibleBounds
        )
        listener?.onCameraIdle(mapCameraPosition, mapVisibleRegion)
    }

    override fun setOnMapClickListener(listener: OnMapClickListener?) {
    }

    override fun setOnMarkerClickListener(listener: OnMarkerClickListener?) {
        if (!checkReady() && listener != null) {
            LoggerUtils.d(TAG, "setOnMarkerClickListener >>> map fragment or listener is null")
            return
        }

        mMap?.setOnMarkerClickListener { marker ->
            LoggerUtils.d(TAG, "onMarkerClick>>> marker$marker")
            val zIndex: Int = marker.getZIndex()
            marker.setZIndex(zIndex + 1)
            val title = marker.title
            val marker2 = mConvertHelper.convertMapMarker(
                mConvertHelper.convertMapLatLng(
                    marker.getPosition().latitude,
                    marker.getPosition().longitude
                ), title
            )
            listener!!.onMarkerClick(
                marker2, mLastSelectedMarker != null && mLastSelectedMarker == marker
            )
            mLastSelectedMarker = marker
            if (TextUtils.isEmpty(title)) {
                false
            }
//            removeMarkers(mMarkers)
            addCustomMarkers(mMarkers)



            if (mCustomMarker == null) {
                mCustomMarker = BaiduMapCustomMaker(mBaiduMapFragment!!.requireContext())
            }

            if (mNativeMarkers == null) {
                mNativeMarkers = ArrayList<Overlay>()
            }

            val customView: BitmapDescriptor? = mCustomMarker!!.generateCustomMarkerBlue(marker2)
            val latLng: MapLatLng = marker2.getLatLng()
            val lng = LatLng(latLng.latitude, latLng.longitude)

            val marker1: Overlay? =
                mMap?.addOverlay(
                    MarkerOptions().position(lng).icon(customView).title(title).visible(true)
                )
            if (marker1 != null) {
                val info = Bundle()
                info.putDouble("lat", latLng.latitude)
                info.putDouble("lng", latLng.longitude)
                marker1.extraInfo = info
                mNativeMarkers?.add(marker1)
            }

            marker.remove()
            false
        }
    }

    override fun setOnCameraMoveListener(listener: OnCameraMoveListener?) {
    }

    override fun obtainMarkers(): MutableList<MapMarker>? {
        LoggerUtils.d(TAG, "mMarkers:$mMarkers")
        return if (mMarkers == null) null else mMarkers
    }

    override fun removeMarker(marker: MapMarker?) {
        LoggerUtils.w(TAG, "removeMarker >>>  ")
        if (marker == null) {
            LoggerUtils.w(TAG, "removeMarker >>>  marker is null")
            return
        }

        if (!checkReady()) {
            LoggerUtils.w(TAG, "removeMarker >>> AMap is null")
            return
        }

        if (mNativeMarkers != null && mNativeMarkers!!.size > 0) {
            LoggerUtils.w(TAG, "removeMarker >>> for mNativeMarkers")
            for (lat in mNativeMarkers!!) {
                val tag: Bundle? = lat.extraInfo
                if (tag is Bundle) {
                    val tagLat = tag.getDouble("lat")
                    val tagLng = tag.getDouble("lng")
                    if (tagLat == marker.latLng.latitude && tagLng == marker.latLng.longitude) {
                        if (lat.isVisible) {
                            LoggerUtils.w(TAG, "removeMarker:  isVisible")
                            lat.isVisible = false
                        }
                    }
                }
            }
        }
    }

    override fun removeMarkers(markers: MutableList<MapMarker>?) {
        LoggerUtils.w(TAG, "removeMarkers >>> ")
        if (markers == null) {
            LoggerUtils.w(TAG, "removeMarkers >>> markers is null")
            return
        }

        if (!checkReady()) {
            LoggerUtils.w(TAG, "addCustomMarkers >>> mFragment is null or amap is null")
            return
        }

        clearMarker()

    }

    override fun setMapType(type: Int) {
        mBaiduMapFragment?.setMapType(type)
    }


    private fun checkReady(): Boolean {
        return mBaiduMapFragment != null && mMap != null
    }

    override fun onLoadFinish() {
        mMap = mBaiduMapFragment?.mMap
//        defaultSetUp()
        mapReadyCallback?.onMapReady()
    }

    override fun onCreate() {
        LoggerUtils.d(TAG, "onCreate")
    }

    override fun onStart() {
        LoggerUtils.d(TAG, "onStart")
    }

    override fun onResume() {
        LoggerUtils.d(TAG, "onResume")
    }

    override fun onPause() {
        LoggerUtils.d(TAG, "onPause")
    }

    override fun onStop() {
        initData = false
        LoggerUtils.d(TAG, "onStop")

    }

    override fun onDestroy() {
        LoggerUtils.d(TAG, "onDestroy")
    }

}
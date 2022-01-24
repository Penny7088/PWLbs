package com.pw.lib.lbs.baidu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.pw.lib.lbs.map.MapConstants
import com.pw.lib.lbs.log.LoggerUtils
import com.pw.lib.lbs.map.LifecycleObserver

/**
 *  百度地图的fragment
 *
 *  create by Huangzefeng on 1/9/2021
 */
class BaiduMapSupportFragment : Fragment() {

    companion object {
        fun newInstance(): BaiduMapSupportFragment {
            return BaiduMapSupportFragment()
        }
    }

    private var lifecycleObserver: LifecycleObserver? = null
    private var mMapView: TextureMapView? = null
    var mMap: BaiduMap? = null
    var onMapLoadedCallback: OnMapLoadCallback? = null
    private var overlay: Overlay? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_support_baidu, container, false)
        mMapView = view.findViewById(R.id.bmapView)
        mMap = mMapView?.map
        mMapView?.showZoomControls(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMap?.setOnMapLoadedCallback {
            onMapLoadedCallback?.onLoadFinish()
        }
    }

    fun registerLifecycleObserver(lifecycleObserver: LifecycleObserver) {
        this.lifecycleObserver = lifecycleObserver
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
        lifecycleObserver?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
        lifecycleObserver?.onPause()
    }

    override fun onStop() {
        super.onStop()
        lifecycleObserver?.onStop()
    }

    override fun onStart() {
        super.onStart()
        lifecycleObserver?.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    fun focusLocation(latitude: Double, longitude: Double) {
        LoggerUtils.v("BaiduMapSupportFragment", "focusLocation lat:$latitude,lng:$longitude")
        val status = MapStatusUpdateFactory.newLatLng(LatLng(latitude, longitude))
        mMap?.setMapStatus(status)
    }

    fun markerLocation(latitude: Double, longitude: Double, icon: Int) {
        if (overlay != null) {
            val overlayOptions = arrayListOf(overlay!!)
            mMap?.removeOverLays(overlayOptions)
        }
        val lng = LatLng(latitude, longitude)
        val options = MarkerOptions().position(lng).icon(BitmapDescriptorFactory.fromResource(icon))
        overlay = mMap?.addOverlay(options)
    }

    fun setMapType(type: Int) {
        if (type == MapConstants.TYPE_MAP_NORMAL) {
            mMap?.mapType = BaiduMap.MAP_TYPE_NORMAL
        } else if (type == MapConstants.TYPE_MAP_SATELLITE) {
            mMap?.mapType = BaiduMap.MAP_TYPE_SATELLITE
        } else if (type == MapConstants.TYPE_MAP_MIX) {
            mMap?.mapType = BaiduMap.MAP_TYPE_NORMAL
        }
    }

}
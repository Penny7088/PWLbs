package com.pw.lib.lbs.baidu

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.baidu.mapapi.map.BitmapDescriptor
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.pw.lib.lbs.entity.MapMarker
import com.pw.lib.lbs.manager.MapCustomMarker

/**
 *  百度生成自定义 Marker类
 *
 *  create by Huangzefeng on 2/9/2021
 */
class BaiduMapCustomMaker(context: Context) : MapCustomMarker(context) {

    fun fromView(context: Context?, view: View?): BitmapDescriptor {
        val frameLayout = FrameLayout(context!!)
        frameLayout.addView(view)
        frameLayout.isDrawingCacheEnabled = true
        val bitmap = getBitmapFromView(frameLayout)
        val bitmapDescriptor: BitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)
        bitmap.recycle()
        return bitmapDescriptor
    }


    fun generateCustomMarker(marker: MapMarker?): BitmapDescriptor {
        val inflate = mInflater.inflate(R.layout.map_marker_custom_layout, null)
        val title = inflate.findViewById<TextView>(R.id.gmap_marker_title)
        title.text = marker?.title
        return fromView(mContext, inflate)
    }

    fun generateCustomMarkerBlue(marker: MapMarker): BitmapDescriptor? {
        val inflate = mInflater.inflate(R.layout.map_marker_custom_layout_blue, null)
        val title = inflate.findViewById<TextView>(R.id.gmap_marker_title)
        title.text = marker.title
        return fromView(mContext, inflate)
    }
}
package com.pw.lib.lbs.google

import android.content.Context
import com.pw.lib.lbs.manager.MapCustomMarker
import android.widget.FrameLayout
import com.pw.lib.lbs.entity.MapMarker
import com.pw.lib.gmap.R
import android.widget.TextView
import android.view.View
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

/**
 * com.hellotalk.lib.gmap
 *
 * @author : Penny (penny@hellotalk.com)
 * @describe :
 * @date : 4/6/21
 */
class GMapCustomMarker(context: Context?) : MapCustomMarker(context) {
    fun fromView(context: Context?, view: View?): BitmapDescriptor {
        val frameLayout = FrameLayout(context!!)
        frameLayout.addView(view)
        frameLayout.isDrawingCacheEnabled = true
        val bitmap = getBitmapFromView(frameLayout)
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)
        bitmap.recycle()
        return bitmapDescriptor
    }

    fun generateCustomMarker(marker: MapMarker): BitmapDescriptor {
        val inflate = mInflater.inflate(R.layout.map_marker_custom_layout, null)
        val title = inflate.findViewById<TextView>(R.id.gmap_marker_title)
        title.text = marker.title
        return fromView(mContext, inflate)
    }

    fun generateCustomMarkerBlue(marker: MapMarker): BitmapDescriptor {
        val inflate = mInflater.inflate(R.layout.map_marker_custom_layout_blue, null)
        val title = inflate.findViewById<TextView>(R.id.gmap_marker_title)
        title.text = marker.title
        return fromView(mContext, inflate)
    }
}
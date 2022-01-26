package com.pw.lib.amap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;

/**
 * com.pw.lib.amap
 *
 * @author : Penny (penny@pw.com)
 * @describe : 解决 高德 mapView 滑动冲突
 * @date : 4/9/21
 */
public class MapAMapView extends MapView {
    private static final String TAG = "MapAMapView";
    private GestureDetector gestureDetector;

    public MapAMapView(Context context) {
        super(context);
    }

    public MapAMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MapAMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MapAMapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }


}

package com.pw.lib.amap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.hellotalk.lib.amap.R;
import com.pw.lib.lbs.entity.MapMarker;
import com.pw.lib.lbs.manager.MapCustomMarker;

/**
 * com.pw.lib.gmap
 *
 * @author : Penny (penny@pw.com)
 * @describe : 高德生成自定义 Marker类
 * @date : 4/6/21
 */
public class AMapCustomMarker extends MapCustomMarker {


    public AMapCustomMarker(Context context) {
        super(context);
    }

    public BitmapDescriptor fromView(Context context, View view) {
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.addView(view);
        frameLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = getBitmapFromView(frameLayout);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        bitmap.recycle();
        return bitmapDescriptor;
    }


    public BitmapDescriptor generateCustomMarker(MapMarker marker) {
        View inflate = mInflater.inflate(R.layout.map_marker_custom_layout, null);
        TextView title = inflate.findViewById(R.id.gmap_marker_title);
        title.setText(marker.getTitle());
        return fromView(mContext, inflate);
    }
}

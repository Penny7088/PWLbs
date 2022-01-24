package com.pw.lib.lbs.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * com.hellotalk.lib.map.manager
 *
 * @author : Penny (penny@hellotalk.com)
 * @describe : 生成自定义Marker 基类
 * @date : 4/9/21
 */
public abstract class MapCustomMarker {

    protected Context mContext;

    protected LayoutInflater mInflater;

    public MapCustomMarker(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }


    public Bitmap getBitmapFromView(View view) {
        try {
            banTextViewHorizontallyScrolling(view);
            view.destroyDrawingCache();
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            Bitmap bitmap = view.getDrawingCache();
            return bitmap != null ? bitmap.copy(Bitmap.Config.ARGB_8888, false) : null;
        } catch (Throwable ex) {
            return null;
        }
    }

    private void banTextViewHorizontallyScrolling(View view) {
        if (view instanceof ViewGroup) {
            for (int index = 0; index < ((ViewGroup) view).getChildCount(); ++index) {
                banTextViewHorizontallyScrolling(((ViewGroup) view).getChildAt(index));
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setHorizontallyScrolling(false);
        }
    }



}

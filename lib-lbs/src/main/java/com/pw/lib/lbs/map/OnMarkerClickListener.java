package com.pw.lib.lbs.map;

import com.pw.lib.lbs.entity.MapMarker;

/**
 *
 * @author : Penny (penny@hellotalk.com)
 * @describe :
 * @date : 4/8/21
 */
public interface OnMarkerClickListener {

    boolean onMarkerClick(MapMarker marker,boolean isClickSameMarker);
}

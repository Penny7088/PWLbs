package com.pw.lib.lbs.map;

import com.pw.lib.lbs.entity.MapCameraPosition;
import com.pw.lib.lbs.entity.MapVisibleRegion;

/**
 * com.hellotalk.lib.map.map
 *
 * @author : Penny (penny@hellotalk.com)
 * @describe :
 * @date : 4/6/21
 */
public interface OnCameraIdleListener {

    void onCameraIdle(MapCameraPosition position, MapVisibleRegion visibleRegion);
}

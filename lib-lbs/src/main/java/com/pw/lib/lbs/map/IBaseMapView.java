package com.pw.lib.lbs.map;

import android.content.Context;
import android.content.res.Resources;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.pw.lib.lbs.entity.MapLatLng;
import com.pw.lib.lbs.entity.MapMarker;

import java.util.List;

/**
 * com.hellotalk.lib.map.map
 *
 * @author : Penny (penny@hellotalk.com)
 * @describe :
 * @date : 4/2/21
 */
public interface IBaseMapView {

    /**
     * 创建fragment
     *
     * @param isTransmit 是否需要传递参数 后期再完善
     * @return 返回基类的MapView
     */
    IBaseMapView createMapFragment(boolean isTransmit);

    /**
     * 获取地图的fragment
     *
     * @return
     */
    Fragment obtainMapFragment();

    /**
     * 隐藏fragment
     *
     * @param transaction FragmentTransaction
     */
    void hideFragment(FragmentTransaction transaction);

    /**
     * 默认设置
     */
    void defaultSetUp();

    /**
     * 添加自定义多个marker
     *
     * @param markers 集合
     */
    void addCustomMarkers(List<MapMarker> markers);

    /**
     * 添加单个marker
     *
     * @param marker
     */
    void addMarker(MapMarker marker);

    void addIconMarker(MapMarker marker, Resources resource, int iconRes);

    /**
     * 清楚marker
     */
    void clearMarker();

    /**
     * 地图初始化接口
     *
     * @param listener
     */
    void getMapAsync(OnMapReadyCallback listener);

    /**
     * 移动camera
     *
     * @param mapLatLng
     */
    void moveCamera(MapLatLng mapLatLng);

    /**
     * 移动camera 至哪个区域
     *
     * @param mapLatLng
     */
    void moveCameraBounds(List<MapLatLng> mapLatLng);

    /**
     * 是否禁止地图移动
     *
     * @param enabled true 移动 false 不移动
     */
    void setScrollGesturesEnabled(boolean enabled);

    /**
     * 设置最小和最大缩放比
     *
     * @param min
     * @param max
     */
    void setMinMaxZoom(float min, float max);

    void setBestZoom();

    /**
     * 设置我的位置定位
     *
     * @param context
     * @param enabled
     */
    void setMyLocationEnable(Context context, boolean enabled);


    /**
     * 地图移动后回调接口
     *
     * @param listener
     */
    void setOnCameraIdleListener(OnCameraIdleListener listener);

    /**
     * 点击地图 监听器
     *
     * @param listener
     */
    void setOnMapClickListener(OnMapClickListener listener);

    /**
     * 地图标记监听器
     *
     * @param listener
     */
    void setOnMarkerClickListener(OnMarkerClickListener listener);

    void setOnCameraMoveListener(OnCameraMoveListener listener);

    /**
     * 获取地图上的marker
     *
     * @return 返回集合
     */
    List<MapMarker> obtainMarkers();


    /**
     * 删除单个marker
     *
     * @param marker
     */
    void removeMarker(MapMarker marker);

    /**
     * 删除多个marker
     *
     * @param marker
     */
    void removeMarkers(List<MapMarker> marker);


    /**
     * 设置地图类型
     *
     * @param type
     */
    void setMapType(int type);
}

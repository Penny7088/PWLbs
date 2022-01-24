package com.pw.lib.lbs.manager;

import com.pw.lib.lbs.map.IBaseMapView;
import com.pw.lib.lbs.map.OnMapReadyCallback;

import java.util.List;

/**
 * com.hellotalk.lib.map.proxy
 *
 * @author : Penny (penny@hellotalk.com)
 * @describe : 地图生成代理接口
 * @date : 4/1/21
 */
public interface IMapManager {


    /**
     * 初始化ImageLoader
     *
     * @param availableTypes 可以尝试的lbs类型，从0优先开始使用，不可用再尝试下一个
     */
    void initMap(List<String> availableTypes);


    IBaseMapView getMapService();

    void buildMapService();

    IBaseMapView obtainMapFragment();

    IBaseMapView obtainMapView();


    /**
     * 地图初始化接口
     *
     * @param listener
     */
    void getMapAsync(OnMapReadyCallback listener);


}

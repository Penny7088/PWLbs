package com.pw.lib.lbs.manager;

import com.pw.lib.lbs.log.LoggerUtils;
import com.pw.lib.lbs.map.IBaseMapView;
import com.pw.lib.lbs.map.OnMapReadyCallback;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * com.hellotalk.lib.map.manager
 *
 * @author : Penny (penny@hellotalk.com)
 * @describe : 地图管理类
 * @date : 4/2/21
 */
public class MapManager implements IMapManager {

    private IBaseMapView mapInterface;

    private final LinkedHashMap<String, IBaseMapView> supportMapView = new LinkedHashMap<>();
    private List<String> availableTypes;

    public static MapManager getInstance() {
        return SingletonHolder.sInstance;
    }


    private static class SingletonHolder {
        private static final MapManager sInstance = new MapManager();
    }


    public void registerMapView(String type, IBaseMapView mapView) {
        if (mapView != null) {
            supportMapView.put(type, mapView);

            mapInterface = getMapService();
        }
    }

    @Override
    public void initMap(List<String> availableTypes) {
        this.availableTypes = availableTypes;
        buildMapService();
    }

    public void setAvailableTypes(List<String> availableTypes) {
        this.availableTypes = availableTypes;
        mapInterface = null;
        buildMapService();
    }

    @Override
    public IBaseMapView getMapService() {
        LoggerUtils.INSTANCE.i("MapManager", "getMapService availableTypes:" + availableTypes);
        if (availableTypes == null || availableTypes.size() == 0) {
            return mapInterface;
        }

        int index = 0;
        while (index <= availableTypes.size() - 1) {
            String type = availableTypes.get(index);
            if (supportMapView.containsKey(type)) {
                mapInterface = supportMapView.get(type);
                LoggerUtils.INSTANCE.i("MapManager", "getMapService use:" + mapInterface);
                break;
            }

            index += 1;
        }
        return mapInterface;
    }


    @Override
    public void buildMapService() {
        if (mapInterface == null) {
            mapInterface = getMapService();
        }

    }

    @Override
    public IBaseMapView obtainMapFragment() {
        if (mapInterface == null) {
            mapInterface = getMapService();
        }
        if (mapInterface != null) {
            return mapInterface.createMapFragment(true);
        }
        return null;
    }

    @Override
    public IBaseMapView obtainMapView() {
        if (mapInterface == null) {
            mapInterface = getMapService();
        }
        return mapInterface;
    }

    @Override
    public void getMapAsync(OnMapReadyCallback listener) {
        if (mapInterface != null) {
            mapInterface.getMapAsync(listener);
        }
    }


}

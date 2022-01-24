package com.pw.lib.lbs.baidu

import android.app.Application
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.pw.lib.lbs.LbsConstants
import com.pw.lib.lbs.config.BaseModuleConfig
import com.pw.lib.lbs.location.LocationManager
import com.pw.lib.lbs.manager.MapManager

class LbsBaiduConfig : BaseModuleConfig() {
    override fun onCreate(application: Application) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(application)
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL)
        LocationManager.instance.register(LbsConstants.LBS_BAIDU, BaiduLocationPlugin())

        MapManager.getInstance().registerMapView(LbsConstants.LBS_BAIDU, BaiduMapViewImpl())
    }
}
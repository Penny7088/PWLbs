package com.pw.lib.lbs.google

import android.app.Application
import com.pw.lib.lbs.LbsConstants
import com.pw.lib.lbs.config.BaseModuleConfig
import com.pw.lib.lbs.location.LocationManager
import com.pw.lib.lbs.manager.MapManager

class LbsGoogleConfig : BaseModuleConfig() {
    override fun onCreate(application: Application) {
        LocationManager.instance.register(LbsConstants.LBS_GOOGLE, GoogleLocationPlugin())
        MapManager.getInstance().registerMapView(LbsConstants.LBS_GOOGLE, GMapViewImpl())
    }
}
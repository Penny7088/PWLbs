package com.pw.lib.lbs.baidu

import android.content.Context
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.pw.lib.lbs.LbsConstants
import com.pw.lib.lbs.location.ILocationPlugin
import com.pw.lib.lbs.location.LocationConst
import com.pw.lib.lbs.location.LocationResult
import com.pw.lib.lbs.log.LoggerUtils
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class BaiduLocationPlugin : ILocationPlugin() {

    companion object {
        const val TAG = "BaiduLocationPlugin"
    }

    var mLocationClient: LocationClient? = null
    var option: LocationClientOption = LocationClientOption()

    fun initLocation(context: Context, listener: BDLocationListener?) {
        LoggerUtils.i(TAG, "initLocation")
        mLocationClient = LocationClient(context)
        //声明LocationClient类
        mLocationClient?.registerLocationListener(listener)

        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        option.isOpenGps = true
        option.SetIgnoreCacheException(false)
        option.setCoorType("bd09ll")
        option.setOnceLocation(true)
        mLocationClient?.locOption = option
    }


    override fun request(context: Context, continuation: Continuation<LocationResult>) {
        initLocation(context) {
            if (it.latitude != 4.9E-324 && it.longitude != 4.9E-324) {
                continuation.resume(
                    LocationResult(
                        LbsConstants.LBS_BAIDU,
                        0,
                        it.latitude,
                        it.longitude
                    )
                )
            } else {
                continuation.resume(
                    LocationResult(
                        LbsConstants.LBS_BAIDU,
                        LocationConst.ERROR_LOCATION_VALUE,
                        0.0, 0.0
                    )
                )
            }
            mLocationClient?.stop()
        }
        mLocationClient?.start()
    }
}
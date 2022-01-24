package com.pw.lib.lbs.google

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.pw.lib.lbs.location.ILocationPlugin
import com.pw.lib.lbs.location.LocationConst
import com.pw.lib.lbs.location.LocationResult
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class GoogleLocationPlugin : ILocationPlugin() {

    var locationManger: LocationManager? = null
    var handler: Handler? = null
    var timerTask: Runnable? = null
    var locationCallback: LocationListener? = null

    @SuppressLint("MissingPermission")
    override fun request(
        context: Context,
        continuation: Continuation<LocationResult>
    ) {
        locationManger = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManger?.let {
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_COARSE//低精度，如果设置为高精度，依然获取不了location。
            criteria.isAltitudeRequired = false//不要求海拔
            criteria.isBearingRequired = false//不要求方位
            criteria.isCostAllowed = true//允许有花费
            criteria.powerRequirement = Criteria.POWER_LOW//低功耗

            val gpsStatus = it.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val networkStatus = it.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            val providerList = it.getProviders(true)
            var provider = it.getBestProvider(criteria, true) // true 代表从打开的设备中查找

            if (providerList.contains(LocationManager.NETWORK_PROVIDER) && networkStatus) {
                provider = LocationManager.NETWORK_PROVIDER
            } else if (providerList.contains(LocationManager.GPS_PROVIDER) && gpsStatus) {
                provider = LocationManager.GPS_PROVIDER
            }

            if (TextUtils.isEmpty(provider)) {
                continuation.resume(
                    LocationResult(
                        LocationConst.LOCATION_GOOGLE,
                        LocationConst.ERROR_LOCATION_DISABLE,
                        0.0,
                        0.0
                    )
                )
            } else {
                if (handler == null) {
                    handler = Handler(Looper.getMainLooper())
                }
                timerTask = Runnable {
                    locationCallback?.let {
                        locationManger?.removeUpdates(it)
                    }
                    continuation.resume(
                        LocationResult(
                            LocationConst.LOCATION_GOOGLE,
                            LocationConst.ERROR_LOCATION_TIME_OUT,
                            0.0, 0.0
                        )
                    )
                }
                timerTask?.let {
                    handler?.postDelayed(it, 10000)
                }

                locationCallback = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        timerTask?.let {
                            handler?.removeCallbacks(it)
                        }
                        locationManger?.removeUpdates(this)
                        continuation.resume(
                            LocationResult(
                                LocationConst.LOCATION_GOOGLE,
                                0,
                                location.latitude,
                                location.longitude
                            )
                        )
                    }

                    override fun onProviderDisabled(provider: String) {
                        super.onProviderDisabled(provider)
                    }

                    override fun onProviderEnabled(provider: String) {
                        super.onProviderEnabled(provider)
                    }

                }
                locationCallback?.let {
                    locationManger?.requestLocationUpdates(
                        provider!!,
                        0,
                        0f,
                        it,
                        Looper.getMainLooper()
                    )
                }
            }
        }

    }
}
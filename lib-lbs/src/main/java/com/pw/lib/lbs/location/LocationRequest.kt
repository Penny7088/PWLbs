package com.pw.lib.lbs.location

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.pw.lib.lbs.log.LoggerUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * 发起定位请求的实体，可以指定优先使用哪个服务商的定位服务
 */
class LocationRequest(private val activity: AppCompatActivity) {

    companion object {

        const val TAG = "LocationRequest"

        @JvmStatic
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    var typeList: List<String>? = null
    var listener: OnLocationListener? = null
    var disposable: Disposable? = null

    /**
     * 请求定位
     */
    fun request(askPermission: Boolean, onLocationListener: OnLocationListener) {
        LoggerUtils.i(TAG, "request")
        listener = onLocationListener
        if (typeList.isNullOrEmpty()) {
            listener?.onFailure(LocationConst.ERROR_NO_TYPE)
            return
        }
        if (!askPermission) {
            handleRequest()
            return
        }
        //先申请定位权限
        val rxPermissions = RxPermissions(activity)
        disposable = rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe { granted ->
                if (granted) {
                    //根据typeList发起相应的定位请求
                    handleRequest()
                } else {
                    LoggerUtils.w(TAG, "request permission not granted")
                    listener?.onPermissionDenied()
                }
            }
    }

    private fun handleRequest() {
        LoggerUtils.i(TAG, "request permission granted")
        val locationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        val gpsStatus =
            locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        val netWorkStatus =
            locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
        if (gpsStatus || netWorkStatus) {
            doRequestLocation()
        } else {
            listener?.onFailure(LocationConst.ERROR_LOCATION_DISABLE)
        }
    }

    /**
     * 通过协程发起定位请求
     */
    private fun doRequestLocation() {
        GlobalScope.launch {
            var index = 0
            var errorCode = 0
            while (index < typeList!!.size) {
                val type = typeList!![index]
                LoggerUtils.i(TAG, "doRequestLocation start type:$type")
                if (LocationManager.instance.containPlugin(type)) {
                    val resultDeferred = GlobalScope.async {
                        LocationManager.instance.request(type, activity)
                    }
                    val result = resultDeferred.await()
                    if (result.success()) {
                        errorCode = 0
                        LoggerUtils.i(TAG, "doRequestLocation success type:${result.type}")
                        listener?.onLocationUpdated(
                            result.type,
                            result.latitude,
                            result.longitude
                        )
                        break
                    } else {
                        LoggerUtils.w(
                            TAG,
                            "doRequestLocation error type:${result.type} code:${result.code}"
                        )
                        errorCode = result.code
                    }
                } else {
                    LoggerUtils.w(TAG, "doRequestLocation error no type:$type")
                    errorCode = LocationConst.ERROR_NO_TYPE
                }
                index += 1
            }

            if (errorCode != 0) {
                listener?.onFailure(errorCode)
            }
        }
    }

    fun destroy() {
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
    }

    class Builder {

        private lateinit var mActivity: AppCompatActivity

        private var typeList: MutableList<String>? = null

        fun withContext(activity: AppCompatActivity): Builder {
            mActivity = activity
            return this
        }

        /**
         * 指定使用哪些定位服务商，按照list顺序依次去尝试定位请求
         */
        fun by(types: MutableList<String>?): Builder {
            typeList = types
            return this
        }

        /**
         * 指定哪个定位服务商，可多次调用，会加进typeList列表里面，先加的优先尝试
         */
        fun by(type: String): Builder {
            if (typeList == null) {
                typeList = mutableListOf()
            }
            if (typeList?.contains(type) != true) {
                typeList?.add(type)
            }
            return this
        }

        fun build(): LocationRequest {
            val request = LocationRequest(mActivity)
            request.typeList = typeList
            return request
        }

    }
}
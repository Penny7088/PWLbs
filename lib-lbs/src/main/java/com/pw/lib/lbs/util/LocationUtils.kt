package com.pw.lib.lbs.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.provider.Settings
import com.pw.lib.lbs.log.LoggerUtils

class LocationUtils {

    companion object {

        /*经纬度验证常量*/
        const val LATITUDE_INVALID = 361.0
        const val LONGITUDE_INVALID = 361.0

        @JvmStatic
        fun openLocationSetting(requestCode: Int, activity: Activity) {
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                if (requestCode != -1) {
                    activity.startActivityForResult(intent, requestCode)
                } else {
                    activity.startActivity(intent)
                }
            } catch (e: ActivityNotFoundException) {
                LoggerUtils.e("LocationUtils", e)
            }
        }

        /**
         * 判断经纬度是否有效，0,0其实是有位置的，但是高德貌似是经常会返回0,0，所以现在暂时认为是无效的
         *
         * @param lat
         * @param lng
         * @return
         */
        @JvmStatic
        fun isLocationValid(lat: Double, lng: Double): Boolean {
            return lat != LATITUDE_INVALID && lng != LONGITUDE_INVALID && lat != 0.0 && lng != 0.0
        }
    }

}
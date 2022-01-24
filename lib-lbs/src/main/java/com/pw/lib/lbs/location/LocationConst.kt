package com.pw.lib.lbs.location

object LocationConst {

    const val LOCATION_BAIDU = "baidu"

    const val LOCATION_GOOGLE = "google"

    /**
     * 错误码：没有指定定位服务类型
     */
    const val ERROR_NO_TYPE = -1

    /**
     * 错误码：没有打开定位开关
     */
    const val ERROR_LOCATION_DISABLE = -2

    /**
     * 错误码：拿到的经纬度不可用
     */
    const val ERROR_LOCATION_VALUE = -3

    /**
     * 错误码：定位超时
     */
    const val ERROR_LOCATION_TIME_OUT = -4

}
package com.pw.lib.lbs.location

interface OnLocationListener {

    /**
     * 定位成功回调
     *
     * @param type: 使用的定位服务
     * @param latitude 获得到的纬度
     * @param longitude 获得到的经度
     */
    fun onLocationUpdated(type: String, latitude: Double, longitude: Double)

    /**
     * 定位权限被拒绝
     */
    fun onPermissionDenied()

    /**
     * 定位失败
     *
     * @param code 失败错误码
     */
    fun onFailure(code: Int)

}
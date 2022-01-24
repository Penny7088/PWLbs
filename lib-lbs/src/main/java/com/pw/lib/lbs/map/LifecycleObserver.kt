package com.pw.lib.lbs.map

/**
 *com.hellotalk.lib.map.baidu
 * @describe : 地图的生命周期回调
 * @author : Penny (penny@hellotalk.com)
 * @date   : 2021/9/15
 */
interface LifecycleObserver {

    fun onCreate()

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()
}
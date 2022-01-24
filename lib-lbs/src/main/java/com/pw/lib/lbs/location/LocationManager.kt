package com.pw.lib.lbs.location

import android.content.Context
import kotlin.coroutines.suspendCoroutine

/**
 * 定位插件管理类
 */
class LocationManager {

    companion object {

        val instance: LocationManager by lazy {
            LocationManager()
        }

    }

    private val plugins: MutableMap<String, ILocationPlugin> = mutableMapOf()

    fun register(type: String, plugin: ILocationPlugin) {
        plugins[type] = plugin
    }

    fun containPlugin(type: String): Boolean {
        return plugins.containsKey(type)
    }

    suspend fun request(type: String, context: Context): LocationResult {
        return suspendCoroutine {
            plugins[type]?.request(context, it)
        }
    }

}
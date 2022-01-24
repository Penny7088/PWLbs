package com.pw.lib.lbs.config

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.pw.lib.lbs.log.LoggerUtils

object ConfigManager {

    const val TAG = "ConfigManager"

    /**
     * 在Application初始化，加载模块
     */
    fun initConfig(application: Application) {
        initModules(application)
    }

    /**
     * 初始化插件，并调用各模块的onCreate方法。
     *
     * 各模块(module)如果需要自动注册插件到主module，都在各自模块的AndroidManifest注册自己的ModuleConfig，注意ModuleConfig都需要继承BaseModuleConfig
     */
    private fun initModules(application: Application) {
        LoggerUtils.i(TAG, "initModules start")
        val appInfo: ApplicationInfo = application.packageManager.getApplicationInfo(
            application.packageName,
            PackageManager.GET_META_DATA
        )
        val bundle = appInfo.metaData ?: return
        for (name in bundle.keySet()) {
            val value = bundle.get(name)
            if (value?.equals("ModuleConfig") == true) {
                LoggerUtils.i(TAG, "module:$name = $value")
                try {
                    val configClass = Class.forName(name)
                    val config = configClass.newInstance()
                    if (config is BaseModuleConfig) {
                        config.onCreate(application)
                    }
                } catch (e: Exception) {
                    LoggerUtils.e(TAG, e)
                }
            }
        }
    }

}
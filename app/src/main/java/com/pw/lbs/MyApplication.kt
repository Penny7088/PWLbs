package com.pw.lbs

import android.app.Application
import com.pw.lib.lbs.config.ConfigManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        ConfigManager.initConfig(this)
    }

}
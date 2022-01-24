package com.pw.lbs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pw.R
import com.pw.lib.lbs.location.LocationConst
import com.pw.lib.lbs.location.LocationRequest
import com.pw.lib.lbs.location.OnLocationListener
import com.pw.lib.lbs.log.LoggerUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LocationRequest.newBuilder().withContext(this).by(LocationConst.LOCATION_GOOGLE).build()
            .request(true, object : OnLocationListener {
                override fun onLocationUpdated(type: String, latitude: Double, longitude: Double) {
                    LoggerUtils.i(
                        "MainActivity",
                        "onLocationUpdated type=$type,latitude=$latitude,longitude=$longitude"
                    )
                }

                override fun onPermissionDenied() {
                    LoggerUtils.w("MainActivity", "onPermissionDenied")
                }

                override fun onFailure(code: Int) {
                    LoggerUtils.w("MainActivity", "onFailure code=$code")
                }

            })
    }
}
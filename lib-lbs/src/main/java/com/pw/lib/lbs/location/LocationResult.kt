package com.pw.lib.lbs.location

data class LocationResult(
    val type: String,
    val code: Int,
    val latitude: Double,
    val longitude: Double
) {

    fun success(): Boolean {
        return code == 0
    }

}

package com.pw.lib.lbs.location

import android.content.Context
import kotlin.coroutines.Continuation

abstract class ILocationPlugin {

    abstract fun request(
        context: Context,
        continuation: Continuation<LocationResult>
    )

}
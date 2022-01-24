package com.pw.lib.lbs.log

import android.util.Log

class DefaultLogger : ILogger {
    override fun i(tag: String, msg: String) {
        Log.i(tag, msg)
    }

    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    override fun v(tag: String, msg: String) {
        Log.v(tag, msg)
    }

    override fun w(tag: String, msg: String) {
        Log.w(tag, msg)
    }

    override fun w(tag: String, msg: String, throwable: Throwable) {
        Log.w(tag, msg, throwable)
    }

    override fun w(tag: String, throwable: Throwable) {
        Log.w(tag, throwable)
    }

    override fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    override fun e(tag: String, msg: String, throwable: Throwable) {
        Log.e(tag, msg, throwable)
    }

    override fun e(tag: String, throwable: Throwable) {
        Log.e(tag, "", throwable)
    }
}
package com.pw.lib.lbs.log

interface ILogger {

    fun i(tag: String, msg: String)

    fun d(tag: String, msg: String)

    fun v(tag: String, msg: String)

    fun w(tag: String, msg: String)

    fun w(tag: String, msg: String, throwable: Throwable)

    fun w(tag: String, throwable: Throwable)

    fun e(tag: String, msg: String)

    fun e(tag: String, msg: String, throwable: Throwable)

    fun e(tag: String, throwable: Throwable)

}
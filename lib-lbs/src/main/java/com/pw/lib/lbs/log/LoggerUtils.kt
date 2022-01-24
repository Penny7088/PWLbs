package com.pw.lib.lbs.log

object LoggerUtils {

    private var logger: ILogger = DefaultLogger()

    @JvmStatic
    fun configLogger(custom: ILogger) {
        logger = custom
    }

    fun i(tag: String, msg: String) {
        logger.i(tag, msg)
    }

    fun d(tag: String, msg: String) {
        logger.d(tag, msg)
    }

    fun v(tag: String, msg: String) {
        logger.v(tag, msg)
    }

    fun w(tag: String, msg: String) {
        logger.w(tag, msg)
    }

    fun w(tag: String, msg: String, throwable: Throwable) {
        logger.w(tag, msg, throwable)
    }

    fun w(tag: String, throwable: Throwable) {
        logger.w(tag, throwable)
    }

    fun e(tag: String, msg: String) {
        logger.e(tag, msg)
    }

    fun e(tag: String, msg: String, throwable: Throwable) {
        logger.e(tag, msg, throwable)
    }

    fun e(tag: String, throwable: Throwable) {
        logger.e(tag, "", throwable)
    }
}
package com.l.joshassignment.network
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.internal.platform.Platform

internal object MyLoggingInterceptor {
    fun provideOkHttpLogging(): LoggingInterceptor {
        return LoggingInterceptor.Builder()
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request("Request")
            .response("Response")
            .build()
    }
}

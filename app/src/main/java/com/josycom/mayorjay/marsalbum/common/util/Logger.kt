package com.josycom.mayorjay.marsalbum.common.util

import com.josycom.mayorjay.marsalbum.BuildConfig
import timber.log.Timber

object Logger {

    fun init() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}
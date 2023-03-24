package com.josycom.mayorjay.marsalbum

import android.app.Application
import com.josycom.mayorjay.marsalbum.common.util.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MarsAlbumApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
    }

    private fun initLogger() {
        Logger.init()
    }
}
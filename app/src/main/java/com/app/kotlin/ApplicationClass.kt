package com.app.kotlin

import android.app.Application
import android.util.Log
import com.app.kotlin.service.RetrofitInstance
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()

    }

}
package com.app.kotlin

import android.app.Application
import android.util.Log
import com.app.kotlin.service.RetrofitInstance
import dagger.hilt.android.HiltAndroidApp

class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("Application class", "getMovieDetails:" + RetrofitInstance.getInstance())
        Log.d("Application class", "getMovieDetails:" + RetrofitInstance.getInstance())
        Log.d("Application class", "getMovieDetails:" + RetrofitInstance.getInstance())
        Log.d("Application class", "getMovieDetails:" + RetrofitInstance.getInstance())


    }

}
package com.app.kotlin

import android.content.SharedPreferences
import android.util.Log
import javax.inject.Singleton

@Singleton
class LocalStorage constructor(
    private val sharedPref: SharedPreferences
) {

//    companion object {
//        private val localStorage: LocalStorage by lazy { LocalStorage() }
//
//        private var sharedPref: SharedPreferences? = null
//        fun getStorageInstance(context: Context): LocalStorage {
//            sharedPref = context.getSharedPreferences("Kotlindb", Context.MODE_PRIVATE)
//            println(localStorage)
//            return localStorage
//
//        }
//    }

    fun putString(key: String, value: String) {
        Log.d("LocalStorage", "putString: " + sharedPref)
        sharedPref?.edit()?.putString(key, value)?.apply()
    }

    fun getString(key: String): String? {
        Log.d("LocalStorage", "getString: " + sharedPref)

        return sharedPref?.getString(key, "")
    }

    fun contains(key: String): Boolean {
        Log.d("LocalStorage", "contains: " + sharedPref)

        return sharedPref?.contains(key) == true
    }
}
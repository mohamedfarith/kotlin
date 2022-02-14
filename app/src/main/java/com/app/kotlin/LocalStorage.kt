package com.app.kotlin

import android.content.SharedPreferences
import android.util.Log
import javax.inject.Singleton

@Singleton
class LocalStorage constructor(
    private val sharedPref: SharedPreferences
) {

    fun putString(key: String, value: String) {
        sharedPref.edit()?.putString(key, value)?.apply()
    }

    fun getString(key: String): String? {

        return sharedPref.getString(key, "")
    }

    fun contains(key: String): Boolean {

        return sharedPref?.contains(key)
    }
}
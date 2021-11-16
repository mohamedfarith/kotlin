package com.app.kotlin

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class LocalStorage {

    companion object {
        private val localStorage: LocalStorage by lazy { LocalStorage() }

        private var sharedPref: SharedPreferences? = null
        fun getStorageInstance(context: Context): LocalStorage {
            sharedPref = context.getSharedPreferences("Kotlindb", Context.MODE_PRIVATE)
            println(localStorage)
            return localStorage

        }
    }

    fun putString(key: String, value: String) {
        sharedPref?.edit()?.putString(key, value)?.apply()
    }

    fun getString(key: String): String? {
        return sharedPref?.getString(key, "")
    }

    fun contains(key: String): Boolean {
        return sharedPref?.contains(key) == true
    }
}
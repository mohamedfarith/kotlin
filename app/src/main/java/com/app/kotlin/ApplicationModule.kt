package com.app.kotlin

import android.content.Context
import android.content.SharedPreferences
import com.app.kotlin.service.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    lateinit var sharedPreferences: SharedPreferences

    @Provides
    fun provideRetrofitInstance(): ApiInterface {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiInterface::class.java)
    }
    @Provides
    fun provideSharedPreferenceInstance(@ApplicationContext context:Context): LocalStorage {
        sharedPreferences = context.getSharedPreferences("Kotlindb", Context.MODE_PRIVATE)
        return LocalStorage(sharedPreferences)
    }
}
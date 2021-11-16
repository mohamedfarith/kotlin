package com.app.kotlin.service

import com.app.kotlin.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        fun getInstance(): ApiInterface {
            return apiInterface
        }

        private fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()

        }

        private var apiInterface: ApiInterface =
            getRetrofitInstance().create(ApiInterface::class.java)


    }



}

//object RetrofitInstance {
//    fun getInstance(): ApiInterface {
//        return apiInterface
//    }
//
//    private var apiInterface: ApiInterface =
//        getRetrofitInstance().create(ApiInterface::class.java)
//
//
//    private fun getRetrofitInstance(): Retrofit {
//        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create()).build()
//
//    }
//}
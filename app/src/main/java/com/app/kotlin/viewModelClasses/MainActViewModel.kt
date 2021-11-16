package com.app.kotlin.viewModelClasses

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kotlin.Constants
import com.app.kotlin.models.Movies
import com.app.kotlin.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActViewModel : ViewModel() {

    var livedata: MutableLiveData<Movies> = MutableLiveData();

    fun getMovieDetails(number: Int, language: String, year: Int): MutableLiveData<Movies> {
        viewModelScope.launch(Dispatchers.IO) {

            Log.d("MainActViewModel", "getMovieDetails:" + RetrofitInstance.getInstance())
            Log.d("MainActViewModel", "getMovieDetails:" + RetrofitInstance.getInstance())
            Log.d("MainActViewModel", "getMovieDetails:" + RetrofitInstance.getInstance())


            val response = RetrofitInstance.getInstance()
                .getMovieListWithLanguage(Constants.API_KEY, number, "revenue.desc", language, year)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    livedata.value = response.body()
                }
            }
        }



        return livedata
    }

}
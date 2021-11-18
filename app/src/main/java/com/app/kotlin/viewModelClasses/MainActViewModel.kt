package com.app.kotlin.viewModelClasses

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kotlin.Constants
import com.app.kotlin.models.Movies
import com.app.kotlin.service.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MainActViewModel @Inject constructor(
    var apiInterface: ApiInterface
) : ViewModel() {


    var livedata: MutableLiveData<Movies> = MutableLiveData();

    fun getMovieDetails(number: Int, language: String, year: Int): MutableLiveData<Movies> {
        Log.d("MainActViewModel", "getMovieDetails: "+apiInterface)
        Log.d("MainActViewModel", "getMovieDetails: "+apiInterface)
        Log.d("MainActViewModel", "getMovieDetails: "+apiInterface)
        Log.d("MainActViewModel", "getMovieDetails: "+apiInterface)



        viewModelScope.launch(Dispatchers.IO) {
            val response = apiInterface.getMovieListWithLanguage(
                Constants.API_KEY,
                number,
                "revenue.desc",
                language,
                year
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    livedata.value = response.body()
                }
            }
        }



        return livedata
    }

}
package com.app.kotlin.viewModelClasses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kotlin.Constants
import com.app.kotlin.models.Movies
import com.app.kotlin.service.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActViewModel @Inject constructor(
    var apiInterface: ApiInterface
) : ViewModel() {


    private val livedata: MutableLiveData<Movies> by lazy {
        MutableLiveData<Movies>()
    }

    fun getMovieDetails(number: Int, language: String): MutableLiveData<Movies> {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiInterface.getMovieListWithLanguage(
                Constants.API_KEY,
                number,
                "revenue.desc",
                language
            )
            launch(Dispatchers.Main) {
                if (response.isSuccessful) {
                    livedata.value = response.body()
                }
            }
        }
        return livedata
    }

}
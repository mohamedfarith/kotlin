package com.app.kotlin.viewModelClasses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.log


@HiltViewModel
class DummyViewModelClass @Inject constructor(): ViewModel() {

    /**
     * creating 3 time taking tasking to understand how coroutines work
     *
     */

    init {
        viewModelScope.launch(Dispatchers.Main) {
//            performTaskOne();
//            performTaskTwo();
//            performTaskThree();


            performAsyncTaskOne()
            performAsyncTaskTwo()
            performAsyncTaskThree()
//            var job = performAsyncTaskOne().await()+ performAsyncTaskTwo().await()+performAsyncTaskThree().await()
        }


    }

    fun performAsyncTaskOne():Deferred<String>{
      var result =  viewModelScope.async {
            performTaskOne();
        }
        return result;
    }
    fun performAsyncTaskTwo():Deferred<String>{
        var result =  viewModelScope.async {
            performTaskTwo();
        }
        return result;
    }

    fun performAsyncTaskThree():Deferred<String>{
        var result =  viewModelScope.async {
            performTaskThree();
        }
        return result
    }


    suspend fun performTaskOne(): String {

        delay(2000)
        Log.d("DummyViewModelClass", "task 1 work done: ")
        return ""
    }

    suspend fun performTaskTwo(): String {
        delay(2000)
        Log.d("DummyViewModelClass", "task 2 work done ")
        return ""
    }

    suspend fun performTaskThree(): String {
        delay(2000)
        Log.d("DummyViewModelClass", "task 3 work done ")
        return ""
    }

}
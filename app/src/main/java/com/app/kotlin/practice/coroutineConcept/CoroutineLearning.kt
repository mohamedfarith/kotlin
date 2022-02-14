package com.app.kotlin.practice.coroutineConcept

import kotlinx.coroutines.*

class CoroutineLearning {

    /*Coroutine has three main components
    * 1) Coroutine scope
    * 2) Coroutine builder
    * 3) Dispatcher
    * Coroutine Scope - describes the scope of the coroutine
    * example runBlocking coroutine runs in main thread
    * so we have 3 types - GlobalScope, ViewModelScope and LifecycleScope
    *
    * Global scope -A global CoroutineScope not bound to any job.
    * Global scope is used to launch top-level coroutines which are operating
    * on the whole application lifetime and are not cancelled prematurely.
    *
    * Coroutine builder - we have 2 coroutine builders Launch and Async
    *
    * launch - execute a set of functions one by one with callback methods executing
    * the long running tasks one by one line by line with the help of suspend function.
    * will return job variable instantly and we can wait for the launch
    * to complete by calling join() method
    *
    * async - does the same job as launch but will return a deferred job which we can consider
    * as result of the async coroutine and can be used for future cases.
    * we can wait for the coroutine to complete by calling await() method
    *
    * Dispatcher - Main Dispatcher, IO Dispatcher, Default Dispatcher, UnConfined Dispatcher
    *
    * Main Dispatcher - It starts the coroutine in the main thread. It is mostly used when we
    * need to perform the UI operations within the coroutine, as UI can only be changed from the
    * main thread(also called the UI thread).
    *
    * IO Dispatcher- It starts the coroutine in the IO thread, it is used to perform all the data
    * operations such as networking, reading, or writing from the database, reading, or writing to
    * the files eg: Fetching data from the database is an IO operation, which is done on the IO thread.
    *
    *
    *
    * */

    fun executeGlobalScope(){

        //Global scope defaulty uses Dispatchers.Default to do the operation
        GlobalScope.launch(Dispatchers.Default) {

        }
    }

    suspend fun executeWithContext(){
        withContext(Dispatchers.Main){

        }
    }


}
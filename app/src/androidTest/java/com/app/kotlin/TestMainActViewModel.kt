package com.app.kotlin

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.kotlin.viewModelClasses.MainActViewModel
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestMainActViewModel : TestCase() {

    private lateinit var viewModel: MainActViewModel
    private lateinit var lifeCyclerOwner: LifecycleOwner
    private lateinit var activity: ActivityScenario<MainActivity>

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = MainActViewModel()
        var context = ApplicationProvider.getApplicationContext<Context>()
        var intent = Intent(context,MainActivity::class)
        activity  = ActivityScenario.launch(MainActivity::class)
        lifeCyclerOwner = ApplicationProvider.getApplicationContext<Context>()


    }

    @Test
    private fun testMovieDetails() {
        viewModel.getMovieDetails(1, "ta", 2020).observe(lifeCyclerOwner, { movies ->
            run {
                movies.let { assertTrue(it != null) }
            }
        })
    }

}
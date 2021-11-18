package com.app.kotlin

import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.kotlin.adapters.MovieListAdapter
import com.app.kotlin.databinding.ActivityMainBinding
import com.app.kotlin.models.MovieDetails
import com.app.kotlin.models.Movies
import com.app.kotlin.viewModelClasses.MainActViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val TAG = "MainActivity"
    var movieDetailsArrayList = ArrayList<MovieDetails>()
    var pastVisibleItems = 0
    var visibleItemCount = 0
    var totalItemConut = 0
    var isScrolling = false
    var pageNumber = 1
    var year = 2020
    var language = "ta"
    lateinit var layoutManager: LinearLayoutManager;
    lateinit var movieListAdapter: MovieListAdapter
    private val mainViewModel: MainActViewModel by viewModels()

    @Inject
    lateinit var localStorage: LocalStorage;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this@MainActivity)
        mainBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        mainBinding.loadMoreProgressbar.getIndeterminateDrawable()
            .setColorFilter(
                ContextCompat.getColor(this, R.color.teal_700),
                PorterDuff.Mode.SRC_IN
            )
        mainBinding.progressBar.getIndeterminateDrawable()
            .setColorFilter(
                ContextCompat.getColor(this, R.color.teal_700),
                PorterDuff.Mode.SRC_IN
            )
        mainBinding.progressBar.setVisibility(View.VISIBLE)
        displayElements()


    }

    fun showFilterScreen(view: View) {
        val filterFragment = FilterFragment()
        filterFragment.show(supportFragmentManager, "FILTER_SCREEN")
    }

    private fun displayElements() {
        if (isNetworkAvailable()) {
            mainBinding.progressBar.visibility = View.VISIBLE
            movieListAdapter = MovieListAdapter(movieDetailsArrayList)
            mainBinding.adapter = movieListAdapter
            layoutManager = mainBinding.movieListView.layoutManager as LinearLayoutManager
            getDataFromApi(pageNumber)
        } else {
            mainBinding.progressBar.visibility = View.INVISIBLE
            mainBinding.progressBar.let {
                Snackbar.make(it, "No internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction(
                        "Try Again"
                    ) { displayElements() }.show()
            }
        }
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    //making network call to the movie db api
    fun getDataFromApi(number: Int) {
        if (localStorage.contains(Constants.SELECTED_YEAR)) {
            year = Integer.parseInt(
                localStorage?.getString(Constants.SELECTED_YEAR)
            )
        }
        if (localStorage.contains(Constants.SELECTED_LANGUAGE)
        ) {
            language = localStorage.getString(Constants.SELECTED_LANGUAGE)!!

        }
        mainViewModel.getMovieDetails(number, language, year).observe(this, { movies ->
            run {
                bindData(movies)
            }
        })
    }

    fun bindData(movies: Movies) {
        val page = movies.page
        if (page != "") {
            Log.d(TAG, "into movies obj: $page")
            addMovieDetails(movies)
            mainBinding.progressBar.visibility = View.INVISIBLE
            mainBinding.loadMoreProgressbar.visibility = View.GONE
        }
        mainBinding.movieListView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = layoutManager.childCount
                Log.d(TAG, "onScrolled: child count is $visibleItemCount")
                totalItemConut = layoutManager.itemCount
                Log.d(TAG, "onScrolled: total item count is $totalItemConut")
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                Log.d(
                    TAG,
                    "onScrolled: first visible item position $pastVisibleItems"
                )
                if (dy > 0) {
                    if (isScrolling) {
                        if (visibleItemCount + pastVisibleItems == totalItemConut) {
                            mainBinding.loadMoreProgressbar.visibility = View.VISIBLE
                            getDataFromApi(pageNumber)
                            isScrolling = false
                        }
                    }
                }
            }
        })
    }

    //getting the values of the response from api
    fun addMovieDetails(movies: Movies?) {
        val size = movies?.results?.size
        val movieDetails = ArrayList<MovieDetails>()
        if (size != null)
            for (i in 0 until size) {
                Log.d(TAG, "fetchMovieDetails: ")
                movies.results?.get(i)?.let { movieDetails.add(it) }
            }
        movieDetailsArrayList.addAll(movieDetails)
        movieListAdapter.notifyItemRangeChanged(movieDetailsArrayList.size, 20)
        pageNumber++
    }
}
package com.app.kotlin

import android.content.Intent
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.kotlin.adapters.MovieListAdapter
import com.app.kotlin.databinding.ActivityMainBinding
import com.app.kotlin.models.DetailClass
import com.app.kotlin.models.MovieDetails
import com.app.kotlin.models.Movies
import com.app.kotlin.viewModelClasses.MainActViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.branch.referral.Branch
import io.branch.referral.Branch.BranchReferralInitListener
import io.branch.referral.BranchError
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.validators.IntegrationValidator
import org.json.JSONObject
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val tag = "MainActivity"
    private var movieDetailsArrayList = ArrayList<MovieDetails>()
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemConut = 0
    private var isScrolling = false
    private var pageNumber = 1
    private var language = ""
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var movieListAdapter: MovieListAdapter
    private val mainViewModel: MainActViewModel by viewModels()

    @Inject
    lateinit var localStorage: LocalStorage


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        // if activity is in foreground (or in backstack but partially visible) launching the same
        // activity will skip onStart, handle this case with reInitSession
        if (intent != null &&
            intent.hasExtra("branch_force_new_session") &&
            intent.getBooleanExtra("branch_force_new_session", false)
        ) {
            Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit();
        }
    }

    private val branchReferralInitListener =
        BranchReferralInitListener { linkProperties, error ->
            // do stuff with deep link data (nav to page, display content, etc)

            error?.let {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
            linkProperties?.let {
                handleDeeplinkData(it)
                displayElements()
            }
        }

    private fun handleDeeplinkData(data: JSONObject) {
        if (data.has("\$deeplink_path")) {
            val value = data.get("\$deeplink_path") as String
            if (!TextUtils.isEmpty(value)) {
                val languageClass = Gson().fromJson(value, DetailClass::class.java)
                localStorage.putString(Constants.SELECTED_LANGUAGE, languageClass.language)
                localStorage.getString(Constants.SELECTED_LANGUAGE)?.let {
                    UtilsClass.addBranchEvent(
                        this, BRANCH_STANDARD_EVENT.LOGIN, "language",
                        it
                    )
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        IntegrationValidator.validate(applicationContext)
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).withData(intent.data)
            .init();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this@MainActivity)
        mainBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        mainBinding.loadMoreProgressbar.indeterminateDrawable
            .setColorFilter(
                ContextCompat.getColor(this, R.color.teal_700),
                PorterDuff.Mode.SRC_IN
            )
        mainBinding.progressBar.indeterminateDrawable
            .setColorFilter(
                ContextCompat.getColor(this, R.color.teal_700),
                PorterDuff.Mode.SRC_IN
            )
        mainBinding.progressBar.visibility = View.VISIBLE
        setSupportActionBar(mainBinding.myToolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_actvivty_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> {
                shareAppLink()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun shareAppLink() {
        val selectedLanguage = localStorage.getString(Constants.SELECTED_LANGUAGE)
        val detailsClass = selectedLanguage?.let { DetailClass(it) }

        detailsClass?.let {
            LayoutUtils.getInstance.generateShareSheetDialog(
                this@MainActivity, it,
                object : Branch.BranchLinkShareListener {
                    override fun onShareLinkDialogLaunched() {
                        UtilsClass.addBranchEvent(
                            this@MainActivity,
                            Constants.SS_LAUNCHED, "", ""
                        )
                    }

                    override fun onShareLinkDialogDismissed() {
                        UtilsClass.addBranchEvent(
                            this@MainActivity,
                            Constants.SS_DISSMISSED,
                            "",
                            ""
                        )
                    }

                    override fun onLinkShareResponse(
                        sharedLink: String?,
                        sharedChannel: String?,
                        error: BranchError?
                    ) {
                        UtilsClass.addBranchEvent(
                            this@MainActivity,
                            Constants.LINK_SHARED,
                            "CLICK",
                            "SHARE"
                        )

                    }

                    override fun onChannelSelected(channelName: String?) {
                        UtilsClass.addBranchEvent(
                            this@MainActivity,
                            Constants.SS_CHANNEL_SELECTED,
                            "CLICK",
                            "SHARE"
                        )
                    }

                })

        }

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
        mainBinding.movieListView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = layoutManager.childCount
                totalItemConut = layoutManager.itemCount
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
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

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    //making network call to the movie db api
    private fun getDataFromApi(number: Int) {
        if (localStorage.contains(Constants.SELECTED_LANGUAGE)) {
            language = localStorage.getString(Constants.SELECTED_LANGUAGE)!!

        }
        mainViewModel.getMovieDetails(number, language).observe(this, { movies ->
            if (movies != null) {
                bindData(movies)
            } else {
                mainBinding.loadMoreProgressbar.visibility = View.INVISIBLE
                Toast.makeText(this@MainActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun bindData(movies: Movies) {
        val page = movies.page
        if (page != "") {
            addMovieDetails(movies)
            mainBinding.progressBar.visibility = View.INVISIBLE
            mainBinding.loadMoreProgressbar.visibility = View.GONE
        }

    }

    //getting the values of the response from api
    private fun addMovieDetails(movies: Movies?) {
        val size = movies?.results?.size
        val movieDetails = ArrayList<MovieDetails>()
        if (size != null)
            for (i in 0 until size) {
                movies.results?.get(i)?.let { movieDetails.add(it) }
            }
        movieDetailsArrayList.addAll(movieDetails)
        movieListAdapter.notifyItemRangeChanged(movieDetailsArrayList.size, 20)
        pageNumber++
    }
}
package com.app.kotlin

import android.content.Intent
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.Branch.BranchReferralInitListener
import io.branch.referral.BranchError
import io.branch.referral.SharingHelper
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchEvent
import io.branch.referral.util.LinkProperties
import io.branch.referral.util.ShareSheetStyle
import io.branch.referral.validators.IntegrationValidator
import org.json.JSONObject
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val tag = "MainActivity"
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
                addBranchEvent()
            }
        }

    }

    private fun addBranchEvent() {
        BranchEvent(BRANCH_STANDARD_EVENT.LOGIN)
            .addCustomDataProperty("language", localStorage.getString(Constants.SELECTED_LANGUAGE))
            .logEvent(this)
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
        val linkProperties = LinkProperties().apply {
            channel = "g_drive"
            campaign = "task"
            feature = "marketing"
            addControlParameter("\$android_url", resources.getString(R.string.branch_host))
            addControlParameter(
                "\$deeplink_path",
                Gson().toJson(localStorage.getString(Constants.SELECTED_LANGUAGE)?.let { language ->
                    DetailClass(language)
                })
            )
        }
        val shareSheetStyle =
            ShareSheetStyle(this@MainActivity, "Check this out!", "This stuff is awesome: ")
                .setCopyUrlStyle(
                    ContextCompat.getDrawable(this@MainActivity, android.R.drawable.ic_menu_send),
                    "Copy",
                    "Added to clipboard"
                )
                .setMoreOptionStyle(
                    ContextCompat.getDrawable(this@MainActivity, android.R.drawable.ic_menu_search),
                    "Show more"
                )
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share With")

        BranchUniversalObject().apply {
            canonicalIdentifier
            title
            setContentDescription("data")
            showShareSheet(
                this@MainActivity,
                linkProperties,
                shareSheetStyle,
                object : Branch.BranchLinkShareListener {
                    override fun onShareLinkDialogLaunched() {
                    }

                    override fun onShareLinkDialogDismissed() {
                    }

                    override fun onLinkShareResponse(
                        sharedLink: String?,
                        sharedChannel: String?,
                        error: BranchError?
                    ) {
                    }

                    override fun onChannelSelected(channelName: String?) {
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
                Log.d(tag, "onScrolled: child count is $visibleItemCount")
                totalItemConut = layoutManager.itemCount
                Log.d(tag, "onScrolled: total item count is $totalItemConut")
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                Log.d(
                    tag,
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

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    //making network call to the movie db api
    private fun getDataFromApi(number: Int) {
        if (localStorage.contains(Constants.SELECTED_YEAR)) {
            year = Integer.parseInt(
                localStorage?.getString(Constants.SELECTED_YEAR)
            )
        }
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
            Log.d(tag, "into movies obj: $page")
            addMovieDetails(movies)
            mainBinding.progressBar.visibility = View.INVISIBLE
            mainBinding.loadMoreProgressbar.visibility = View.GONE
        }

    }

    //getting the values of the response from api
    fun addMovieDetails(movies: Movies?) {
        val size = movies?.results?.size
        val movieDetails = ArrayList<MovieDetails>()
        if (size != null)
            for (i in 0 until size) {
                Log.d(tag, "fetchMovieDetails: ")
                movies.results?.get(i)?.let { movieDetails.add(it) }
            }
        movieDetailsArrayList.addAll(movieDetails)
        movieListAdapter.notifyItemRangeChanged(movieDetailsArrayList.size, 20)
        pageNumber++
    }
}
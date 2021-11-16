package com.app.kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.kotlin.R
import com.app.kotlin.databinding.MovieRecyclerViewBinding
import com.app.kotlin.models.MovieDetails
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Inject


class MovieListAdapter constructor(
    var movieDetailsArrayList: ArrayList<MovieDetails>
) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: MovieRecyclerViewBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.movie_recycler_view,
                viewGroup,
                false
            )
        return ViewHolder(view);
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.recyclerViewBinding.movieDetails = movieDetailsArrayList[position]
    }

    override fun getItemCount(): Int {
        return movieDetailsArrayList.size
    }

    inner class ViewHolder(binding: MovieRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var recyclerViewBinding: MovieRecyclerViewBinding;

        init {
            recyclerViewBinding = binding;
        }
    }
}
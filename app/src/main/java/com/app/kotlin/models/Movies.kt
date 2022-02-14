package com.app.kotlin.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Movies() : Serializable {
    @SerializedName("page")
    var page: String? = null

    @SerializedName("total_results")
    var totalResults: String? = null

    @SerializedName("total_pages")
    var totalPages: String? = null

    @SerializedName("results")
    var results: ArrayList<MovieDetails>? = null
}



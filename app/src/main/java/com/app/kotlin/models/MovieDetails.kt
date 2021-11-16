package com.app.kotlin.models

import com.app.kotlin.Constants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class MovieDetails : Serializable {

    @SerializedName("vote_count")
    @Expose
    var voteCount: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("video")
    @Expose
    var video: String? = null

    @SerializedName("vote_average")
    @Expose
    var voteverage: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("popularity")
    @Expose
    var popularity: String? = null

    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null

    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null

    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null

    @SerializedName("genreId")
    @Expose
    var genreId: List<String>? = null

    @SerializedName("backdrop_path")
    @Expose
    var backDropPath: String? = null

    @SerializedName("adult")
    @Expose
    var adult: String? = null

    @SerializedName("overview")
    @Expose
    var overview: String? = null

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null

    @SerializedName("spoken_languages")
    @Expose
    var spokenLanguages: List<SpokenLanguages>? = null


    fun getDisplayBannerText(): String {
        return "$originalTitle ($releaseDate)"
    }

    fun getReleaseYear(): String {
        val year = releaseDate!!.split("-".toRegex()).toTypedArray()
        return year[0]
    }

    fun getFormattedBackDrop(): String {
        return Constants.BASE_BACKDROP_URL + backDropPath
    }

    class SpokenLanguages {
        @SerializedName("name")
        @Expose
        var name: String? = null
    }
}
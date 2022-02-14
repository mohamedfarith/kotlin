package com.app.kotlin.service;

import com.app.kotlin.models.MovieDetails
import com.app.kotlin.models.Movies
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {
    @GET("discover/movie")
    fun getMovieDetails(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String
    ): Call<Movies>

    @GET("discover/movie")
    suspend fun getMovieListWithLanguage(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String,
        @Query("with_original_language") language: String
    ): Response<Movies>

    @GET("movie/{movie_id}")
    fun getDetails(
        @Path("movie_id") movieId: String?,
        @Query("api_key") apiKey: String?
    ): Call<MovieDetails?>?

}

package com.app.kotlin

class Constants {

    companion object {
        val API_KEY = "0e12101a22c608993caa890e9dabea92"
        val BASE_URL = "https://api.themoviedb.org/3/"
        val BASE_BACKDROP_URL = "https://image.tmdb.org/t/p/original"

        val LANGUAGE_LIST: Map<String, String> = mapOf(
            "Tamil" to "ta",
            "English" to "en"
        )
        val YEAR_LIST: Map<String, Int> = mapOf("2020" to 2020, "2019" to 2019)

        val SELECTED_LANGUAGE = "SELECTED_LANGUAGE"
        val SELECTED_YEAR="SELECTED_YEAR"
    }
}
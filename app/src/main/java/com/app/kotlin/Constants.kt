package com.app.kotlin

object Constants {


    const val SS_CHANNEL_SELECTED = "SS_CHANNEL_SELECTED"
    const val LINK_SHARED = "LINK_SHARED"
    const val SS_DISSMISSED = "SS_DISSMISSED"
    const val SS_LAUNCHED = "SS_LAUNCHED"
    const val API_KEY = "0e12101a22c608993caa890e9dabea92"
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_BACKDROP_URL = "https://image.tmdb.org/t/p/original"

    val LANGUAGE_LIST: Map<String, String> = mapOf(
        "Tamil" to "ta",
        "English" to "en"
    )
    val YEAR_LIST: Map<String, Int> = mapOf("2020" to 2020, "2019" to 2019)

    const val SELECTED_LANGUAGE = "SELECTED_LANGUAGE"
    const val SELECTED_YEAR = "SELECTED_YEAR"

}
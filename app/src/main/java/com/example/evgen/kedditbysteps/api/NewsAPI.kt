package com.example.evgen.kedditbysteps.api

import retrofit2.Call

/**
 * News API
 *
 * @author juancho.
 */
interface NewsAPI {
    fun getNews(after: String, limit: String): Call<RedditNewsResponse>
}
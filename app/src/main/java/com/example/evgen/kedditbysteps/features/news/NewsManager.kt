package com.example.evgen.kedditbysteps.features.news

import com.example.evgen.kedditbysteps.api.RestAPI
import com.example.evgen.kedditbysteps.commons.RedditNews
import com.example.evgen.kedditbysteps.commons.RedditNewsItem
import rx.Observable

/**
 * News Manager allows you to request more news from Reddit.
 *
 * @author juancho
 */
class NewsManager(private val api: RestAPI = RestAPI()) {

    /**
     *
     * Returns Reddit News paginated by the given limit.
     *
     * @param after indicates the next page to navigate.
     * @param limit the number of news to request.
     */
    fun getNews(after: String, limit: String = "10"): Observable<RedditNews> {
        return Observable.unsafeCreate {
            subscriber ->
            val callResponse = api.getNews(after, limit)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                val dataResponse = response.body()?.data
                val news = dataResponse!!.children.map {
                    val item = it.data
                    RedditNewsItem(item.author, item.title, item.num_comments,
                            item.created, item.thumbnail, item.url)
                }
                val redditNews = RedditNews(
                        dataResponse.after ?: "",
                        dataResponse.before ?: "",
                        news)

                subscriber.onNext(redditNews)
                subscriber.onCompleted()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}
package com.example.evgen.kedditbysteps.features.news

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.evgen.kedditbysteps.R
import com.example.evgen.kedditbysteps.commons.InfiniteScrollListener
import com.example.evgen.kedditbysteps.commons.RedditNews
import com.example.evgen.kedditbysteps.commons.RxBaseFragment
import com.example.evgen.kedditbysteps.commons.extensions.inflate
import com.example.evgen.kedditbysteps.features.news.adapter.NewsAdapter
import kotlinx.android.synthetic.main.news_fragment.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class NewsFragment : RxBaseFragment() {

    private var redditNews: RedditNews? = null
    private val newsManager by lazy { NewsManager() }

    companion object {
        private val KEY_REDDIT_NEWS = "redditNews"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.news_fragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /*news_list.setHasFixedSize(true)
        val linearLayout = LinearLayoutManager(context)
        news_list.layoutManager = linearLayout
        news_list.clearOnScrollListeners()
        news_list.addOnScrollListener(InfiniteScrollListener({ requestNews() }, linearLayout))*/

        news_list.apply {
            setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener({ requestNews() }, linearLayout))
        }

        initAdapter()

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_REDDIT_NEWS)) {
            redditNews = savedInstanceState.get(KEY_REDDIT_NEWS) as RedditNews
            (news_list.adapter as NewsAdapter).clearAndAddNews(redditNews!!.news)
        } else {
            requestNews()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val news = (news_list.adapter as NewsAdapter).getNews()
        if (redditNews != null && news.size > 0) {
            outState.putParcelable(KEY_REDDIT_NEWS, redditNews?.copy(news = news))
        }
    }

    private fun requestNews() {
        /**
         * first time will send empty string for after parameter.
         * Next time we will have redditNews set with the next page to
         * navigate with the after param.
         */
        val subscription = newsManager.getNews(redditNews?.after ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { retrievedNews ->
                            redditNews = retrievedNews
                            (news_list.adapter as NewsAdapter).addNews(retrievedNews.news)
                        },
                        { e ->
                            Snackbar.make(news_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                        }
                )
        subscriptions.add(subscription)
    }

    private fun initAdapter() {
        if (news_list.adapter == null) {
            news_list.adapter = NewsAdapter()
        }
    }
}
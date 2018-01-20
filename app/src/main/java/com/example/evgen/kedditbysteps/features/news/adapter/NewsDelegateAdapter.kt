package com.example.evgen.kedditbysteps.features.news.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.evgen.kedditbysteps.R
import com.example.evgen.kedditbysteps.commons.RedditNewsItem
import com.example.evgen.kedditbysteps.commons.adapter.ViewType
import com.example.evgen.kedditbysteps.commons.adapter.ViewTypeDelegateAdapter
import com.example.evgen.kedditbysteps.commons.extensions.getFriendlyTime
import com.example.evgen.kedditbysteps.commons.extensions.inflate
import com.example.evgen.kedditbysteps.commons.extensions.loadImg
import kotlinx.android.synthetic.main.news_item.view.*

class NewsDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return TurnsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as TurnsViewHolder
        holder.bind(item as RedditNewsItem)
    }

    class TurnsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.news_item)) {

        fun bind(item: RedditNewsItem) = with(itemView) {
            //Picasso.with(itemView.context).load(item.thumbnail).into(img_thumbnail)
            img_thumbnail.loadImg(item.thumbnail)
            description.text = item.title
            author.text = item.author
            val commentsText = context.resources.getString(R.string.comments)
            comments.text = "${item.numComments} $commentsText"
            time.text = item.created.getFriendlyTime()
        }
    }
}
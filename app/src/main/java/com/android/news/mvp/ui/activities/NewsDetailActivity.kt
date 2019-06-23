package com.android.news.mvp.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.android.news.R
import com.android.news.data.models.entity.NewsItem
import com.android.news.mvp.ui.fragments.MainFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetailActivity : AppCompatActivity() {
    var newsItem: NewsItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        intent?.let {
            newsItem = it.getParcelableExtra(MainFragment.ARG_PARAM1)
        }
        newsItem.let {
            Glide.with(this).load(newsItem?.urlToImage).placeholder(R.drawable.news_placeholder).into(ivImage)
        }
        tvTitle?.text = Html.fromHtml(newsItem?.title)
        tvDesc?.text = Html.fromHtml(newsItem?.description)

    }
}

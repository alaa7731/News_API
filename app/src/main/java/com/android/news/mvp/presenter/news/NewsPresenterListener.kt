package com.android.news.mvp.presenter.news

import com.android.news.data.models.entity.NewsItem
import com.android.news.mvp.presenter.BasePresenterListener

interface NewsPresenterListener : BasePresenterListener {

    fun onNewsLoaded(data: List<NewsItem>)
}
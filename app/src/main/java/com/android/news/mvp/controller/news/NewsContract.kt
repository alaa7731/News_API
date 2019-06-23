package com.android.news.mvp.controller.news

import com.android.news.data.models.entity.NewsItem
import com.android.news.mvp.presenter.BasePresenterContract

interface NewsContract : BasePresenterContract {
    fun getNews(country: String, apiKey: String, success: (List<NewsItem>) -> Unit?, failure: () -> Unit)
}
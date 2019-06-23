package com.android.news.mvp.presenter.news

import android.content.Context
import com.android.news.data.api.CallAPIs
import com.android.news.mvp.controller.news.NewsContract
import com.android.news.mvp.controller.news.NewsController
import com.android.news.mvp.presenter.BasePresenter
import com.android.news.network.queue.RetrofitBuilder
import javax.inject.Inject

class NewsPresenter @Inject constructor(
    context: Context,
    callAPIs: CallAPIs,
    retrofitBuilder: RetrofitBuilder<CallAPIs>
) : BasePresenter<NewsContract, NewsPresenterListener>(context) {
    init {
        controller = NewsController(this, tag, callAPIs, retrofitBuilder)
    }

    fun getNews(country: String, apiKey: String) {
        listener?.showProgress()
        controller?.getNews(country, apiKey, {
            listener?.hideProgress()
            listener?.onNewsLoaded(it)
        }, { listener?.hideProgress() })
    }
}
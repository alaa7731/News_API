package com.android.news.mvp.controller.news

import com.android.news.data.api.CallAPIs
import com.android.news.data.models.BaseResponseModel
import com.android.news.data.models.NewsResponseModel
import com.android.news.data.models.entity.NewsItem
import com.android.news.mvp.controller.BaseController
import com.android.news.mvp.controller.BaseControllerListener
import com.android.news.network.queue.RetrofitBuilder
import com.android.news.network.queue.RetrofitCallback
import com.android.news.network.queue.RetrofitRequest

class NewsController(
    listener: BaseControllerListener, tag: String,
    callAPIs: CallAPIs, retrofitBuilder: RetrofitBuilder<CallAPIs>
) : BaseController<BaseControllerListener>(listener, tag, callAPIs, retrofitBuilder), NewsContract {
    override fun getNews(country: String, apiKey: String, success: (List<NewsItem>) -> Unit?, failure: () -> Unit) {
        val call = callAPIs.getNews(country, apiKey)
        val callback = object : RetrofitCallback<NewsResponseModel>(tag, "GET_NEWS", listener) {
            override fun onSuccess(response: NewsResponseModel) {
                if (!response.data.isNullOrEmpty())
                    success.invoke(response.data!!)
                else failure.invoke()
            }

            override fun onResponse(response: BaseResponseModel) {
                failure.invoke()
            }

        }
        val request = RetrofitRequest(call, callback)
        retrofitBuilder.execute(request)
    }

}
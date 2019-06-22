package com.android.news.mvp.controller

 import com.android.news.data.api.CallAPIs
 import com.android.news.data.models.BaseResponseModel
 import com.android.news.mvp.presenter.BasePresenterContract
 import com.android.news.network.queue.RetrofitBuilder
 import java.io.File

abstract class BaseController<T : BaseControllerListener>
constructor(val listener: T, val tag: String, val callAPIs: CallAPIs,
            val retrofitBuilder: RetrofitBuilder<CallAPIs>
) :
    BasePresenterContract {
    override fun destroy() {
        retrofitBuilder.cancelAllRelated(tag)
    }

     public fun getStatus(response: BaseResponseModel): String =
            response.status!!

}
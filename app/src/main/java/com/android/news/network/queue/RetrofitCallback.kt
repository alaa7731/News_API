package com.android.news.network.queue

import com.android.news.dagger.BaseApplication
import com.android.news.data.models.BaseResponseModel
import com.android.news.mvp.controller.BaseControllerListener
import org.jetbrains.annotations.Nullable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

abstract class RetrofitCallback<T : BaseResponseModel>(
    val label: String,
    val serviceKey: String,
    val listener: BaseControllerListener?
) : Callback<T> {

    val tag: String by lazy {
        "${label}_${serviceKey}"
    }

    val retrofitBuilder by lazy {
        (BaseApplication.appContext as BaseApplication).appComponents.provideRetrofit()
    }

    val gson by lazy {
        (BaseApplication.appContext as BaseApplication).appComponents.provideGson()
    }

    override fun onFailure(call: Call<T>?, t: Throwable?) {
        t?.printStackTrace()
        when (t) {
            is IOException -> handleError(CONNECTION_ERROR, t.message)
            else -> handleError(FAILURE_ERROR, t?.message)
        }
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        var errorMessage = response?.errorBody()?.string()
        response?.body()?.key = tag
        if (response?.isSuccessful() == true) {
            if (response.body() != null && response.body()?.status.equals("ok")) {
                onSuccess(response.body()!!)
                retrofitBuilder.removeRequest(tag)
            } else if (response.code() == 200) {
//                onResponse(BaseResponseModel())
                handleError(response.code(), response.body()?.message)
                retrofitBuilder.removeRequest(tag)
            } else {
                handleError(response.code(), null)
            }
        } else {
            if (errorMessage != null) {
                try {
                    onResponse(gson.fromJson(errorMessage, BaseResponseModel::class.java))
                    retrofitBuilder.removeRequest(tag)
                } catch (ex: IllegalStateException) {
                    handleError(response?.code(), response?.message())

                } catch (ex: Exception) {
                    handleError(response?.code(), response?.message().plus(" Error Code: ".plus(response?.code())))
                }
            } else {
                handleError(response?.code(), response?.message())
            }
        }
    }

    private fun handleError(code: Int?, message: String?) {
        when (code) {
            CONNECTION_ERROR //Connection issues
            -> {
                if ("Socket closed".equals(message, ignoreCase = true)
                    || "Canceled".equals(message, ignoreCase = true)
                ) {
                    retrofitBuilder.stopRunning(tag)
                    return
                }
                onConnectionFailure()
            }
            else -> onFailure(message)
        }
        retrofitBuilder.removeRequest(tag)

    }

    fun onFailure(message: String?) {
        listener?.onFailure(message)
//        onResponse(BaseResponseModel())
    }

    fun onConnectionFailure() {
        listener?.onConnectionFailure()
    }


    abstract fun onSuccess(@Nullable response: T)
    abstract fun onResponse(response: BaseResponseModel)
    fun onNoInternetConnection() {
        listener?.onNoInternetConnection()
    }

    companion object {
        const val CONNECTION_ERROR: Int = 700
        const val FAILURE_ERROR: Int = 800
    }
}
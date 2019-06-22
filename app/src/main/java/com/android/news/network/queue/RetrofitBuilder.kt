package com.android.news.network.queue

import com.android.news.network.helpers.InternetConnectionStatus
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Created by Alaa on 08/12/17.
 */

class RetrofitBuilder<T> private constructor(baseUrl: String, gson: Gson,
                                             okHttpClient: OkHttpClient,
                                             cls: Class<T> ) {
    val apiInterface: T
    val queue = HashMap<String, RetrofitRequest>()

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        apiInterface = retrofit.create(cls)
    }

    fun execute(request: RetrofitRequest) {
        if (queue.containsKey(request.callback.tag)) {
            if (queue.get(request.callback.tag)?.isRunning == false) {
                removeRequest(request.callback.tag)
            } else {
                request.callback.onFailure("Request already running")
                return
            }
        }
        if (!InternetConnectionStatus.getInstance().isConnected) {
            request.callback.onNoInternetConnection()
            return
        }
        queue.put(request.callback.tag, request)
        executeRequest(queue.get(request.callback.tag)!!)
    }

    fun stopRunning(TAG: String) {
        if (queue.containsKey(TAG)) {
            queue.get(TAG)?.setRunning(false)
        }
    }

    private fun cancelAndRemove(TAG: String) {
        cancelRequest(TAG)
        removeRequest(TAG)
    }

    fun removeRequest(TAG: String) {
        if (queue.containsKey(TAG)) {
            queue.get(TAG)?.setRunning(false)
            queue.remove(TAG)
        }
    }

    fun cancelAllRelated(prefixTAG: String) {
        val keys = ArrayList<String>()
        queue.forEach {
            if (it.key.contains(prefixTAG)) {
                keys.add(it.key)
            }
        }
        keys.forEach {
            cancelAndRemove(it)
        }
    }

    private fun cancelRequest(TAG: String) {
        queue.get(TAG)?.setRunning(false)
        queue.get(TAG)?.getRequestCall()?.cancel()
    }

    fun pendingAllRequests() {
        queue.forEach { key, value ->
            cancelRequest(key)
        }
    }

    private fun executeRequest(request: RetrofitRequest) {
        if (!request.isRunning) {
            request.isRunning = true
            try {
                request.requestCall
                        .enqueue(request.callback)
            } catch (ex: IllegalStateException) {
                request.requestCall
                        .clone().enqueue(request.callback)
            }

        }
    }

    fun executeAllPendingRequests() {
        queue.forEach { key, value ->
            executeRequest(value)
        }
    }

    fun removeSuccessRequest(url: String) {
        val keys = ArrayList<String>()
        queue.forEach { key, value ->
            if (value.requestCall.request().url().toString().toLowerCase()
                            .equals(url)) {
                keys.add(key)
            }
        }
        keys.forEach {
            removeRequest(it)
        }
    }

    companion object {

        private val mLock = Any()
        private var mInstance: RetrofitBuilder<*>? = null

        fun <T> getInstance(baseUrl: String, gson: Gson,
                            okHttpClient: OkHttpClient,
                            cls: Class<T>): RetrofitBuilder<T> {
            synchronized(mLock) {
                if (mInstance == null) {
                    mInstance = RetrofitBuilder(baseUrl, gson,
                            okHttpClient, cls)
                }
                return mInstance as RetrofitBuilder<T>
            }
        }
    }

}


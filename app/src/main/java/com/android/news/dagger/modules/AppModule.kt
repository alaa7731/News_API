package com.android.news.dagger.modules

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.android.news.BuildConfig
import com.android.news.dagger.BaseApplication
import com.android.news.data.api.CallAPIs
import com.android.news.network.log.CustomHttpLoggingInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import com.android.news.network.queue.RetrofitBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Singleton

@Module
class AppModule(val app: BaseApplication) {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .create()
    }

    @Singleton
    @Provides
    fun providePreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request: Request
            val builder = original.newBuilder()
            val headers = initializeHeaders()
            headers.forEach {
                builder.header(it.key, it.value)
            }
            builder.method(original.method(), original.body())
            request = builder.build()
            chain.proceed(request)
        }
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = CustomHttpLoggingInterceptor()
            loggingInterceptor.level = CustomHttpLoggingInterceptor.Level.BODY

            httpClient.addInterceptor(loggingInterceptor)
        }
        return httpClient.build()
    }


    private fun initializeHeaders(): HashMap<String, String> {
        val headers = HashMap<String, String>()

        headers.put("Accept", "application/json")
        headers.put("Content-Type", "application/json")
        return headers
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): RetrofitBuilder<CallAPIs> {
        return RetrofitBuilder.getInstance(BuildConfig.BASE_URL,
                gson, okHttpClient, CallAPIs::class.java)
    }

    @Singleton
    @Provides
    fun provideAPIs(retrofitBuilder: RetrofitBuilder<CallAPIs>): CallAPIs {
        return retrofitBuilder.apiInterface
    }
}
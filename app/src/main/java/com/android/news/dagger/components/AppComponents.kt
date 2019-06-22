package com.android.news.dagger.components

import com.google.gson.Gson
import dagger.Component
import com.android.news.dagger.modules.ActivityModule
import com.android.news.dagger.modules.AppModule
import com.android.news.data.api.CallAPIs
import com.android.news.network.queue.RetrofitBuilder
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponents {
    fun provideGson(): Gson

    fun plus(module: ActivityModule): ActivityComponents
    fun provideRetrofit(): RetrofitBuilder<CallAPIs>

}
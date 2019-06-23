package com.android.news.data.api

 import com.android.news.data.models.NewsResponseModel
 import retrofit2.Call
 import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CallAPIs {
    @GET("top-headlines")
    fun getNews(@Query("country") country: String,
                                   @Query("apiKey") apiKey: String): Call<NewsResponseModel>
}
package com.android.news.data.models

import com.android.news.data.models.entity.NewsItem
import com.google.gson.annotations.SerializedName

open class BaseResponseModel(

    var key: String? = "",

    @SerializedName(value = "status")
    val status: String? = "",

    @SerializedName(value = "message")
    val message: String? = "",

    @SerializedName(value = "totalResults")
    var totalResults: Int? = 0
)


data class NewsResponseModel(@SerializedName("articles") var data: List<NewsItem>) : BaseResponseModel()







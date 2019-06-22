package com.android.news.data.models.entity

import android.os.Parcelable
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Generated("com.robohorse.robopojogenerator")
@Parcelize
data class NewsItem(

        @field:SerializedName("author")
        val author: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("url")
        val url: String? = null,

        @field:SerializedName("urlToImage")
        val urlToImage: String? = null,

        @field:SerializedName("publishedAt")
        val publishedAt: Date? = null,

        @field:SerializedName("content")
        val content: String? = null
) : Parcelable
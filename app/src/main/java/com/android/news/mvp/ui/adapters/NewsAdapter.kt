package com.android.news.mvp.ui.adapters

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.news.R
import com.android.news.data.models.entity.NewsItem
import com.bumptech.glide.Glide

class NewsAdapter
    (
    val context: Context,
    val data: ArrayList<NewsItem>,
    var onItemClick: (NewsItem, ImageView) -> Unit
) : RecyclerView.Adapter<NewsViewHolder>() {

    val mInflater: LayoutInflater = LayoutInflater.from(context)
    var tempdata = ArrayList<NewsItem>(0)

    //key for tags
    var ITEM = 1
    var IMAGE_VIEW = 2

    init {
        addItems()
    }

    private fun addItems() {
        Handler().postDelayed({
            tempdata = data
            notifyItemRangeInserted(0, tempdata.size)
        }, 500)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
        NewsViewHolder(mInflater.inflate(R.layout.news_list_item, parent, false))

    override fun getItemCount(): Int = tempdata.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        var item = data[position]

        holder.tvTitle?.text = Html.fromHtml(item.title)
        holder.tvDesc?.text = Html.fromHtml(item.description)
        holder.ivImage?.let {
            Glide.with(context).load(item.urlToImage).placeholder(R.drawable.news_placeholder).into(it)
        }

        //to share multiple items with  click listener
        holder.itemView.setTag(R.id.item, item)
        holder.itemView.setTag(R.id.image_view, holder.ivImage)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(it.getTag(R.id.item) as NewsItem, it.getTag(R.id.image_view) as ImageView)
        }
    }

}

class NewsViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var tvTitle: TextView? = itemView.findViewById(R.id.tvTitle)
    var tvDesc: TextView? = itemView.findViewById(R.id.tvDesc)
    var ivImage: ImageView? = itemView.findViewById(R.id.ivImage)

}
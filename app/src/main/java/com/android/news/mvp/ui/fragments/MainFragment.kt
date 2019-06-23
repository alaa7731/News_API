package com.android.news.mvp.ui.fragments


import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.android.news.R
import com.android.news.dagger.components.FragComponents
import com.android.news.data.models.entity.NewsItem
import com.android.news.mvp.presenter.BasePresenter
import com.android.news.mvp.presenter.news.NewsPresenter
import com.android.news.mvp.presenter.news.NewsPresenterListener
import com.android.news.mvp.ui.BaseFragment
import com.android.news.mvp.ui.activities.NewsDetailActivity
import com.android.news.mvp.ui.adapters.NewsAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : BaseFragment<NewsPresenterListener>(), NewsPresenterListener {
    override fun onNewsLoaded(data: List<NewsItem>) {
        if (!isFinishing()) {
            rvNews.adapter = NewsAdapter(activity!!, data as ArrayList<NewsItem>) { item, view ->
                if (isReadyForFragmentTransaction()) {
                    val intent = Intent(activity, NewsDetailActivity::class.java)
                    intent.putExtra(ARG_PARAM1, item)
                    val options = ActivityOptions
                        .makeSceneTransitionAnimation(activity!!, view, getString(R.string.image_transition))
                    startActivity(intent, options.toBundle())

                }
            }
        }
    }

    override fun mainRequest() {
        presenter.getNews("ae", getString(R.string.api_key))
    }

    override fun getPresenter(): BasePresenter<*, *>? = presenter

    override fun injectComponents(fragComponents: FragComponents) {
        fragComponents.inject(this)
    }

    @Inject
    lateinit var presenter: NewsPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter.listener = this

        mainRequest()
    }

    companion object {
        const val ARG_PARAM1 = "param1"

    }

}

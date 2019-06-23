package com.android.news.mvp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.android.news.dagger.BaseApplication
import com.android.news.dagger.components.ActivityComponents
import com.android.news.dagger.modules.ActivityModule
import com.android.news.data.SessionManagement


/**
 * This is the base activity which holds the common methods
 * shared bet. any activity ..
 */
abstract class BaseActivity : AppCompatActivity() {
    var activityComponents: ActivityComponents? = null
    var mIsSafeToCommitTransactions: Boolean = false
    private lateinit var sessionManagement: SessionManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // eliminate re-init the components in case of configuration changes
        // or low-memory kill case..
        if (savedInstanceState == null || activityComponents == null) {
            setUpComponents()
        }
        mIsSafeToCommitTransactions = true
    }

    override fun onStart() {
        super.onStart()

        mIsSafeToCommitTransactions = true
    }

    override fun onResume() {
        super.onResume()

        mIsSafeToCommitTransactions = true

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mIsSafeToCommitTransactions = false
    }

    /**
     * build the dependency graph for each activity (e.g activity scope ...)
     */
    private fun setUpComponents() {
        this.activityComponents = (application as BaseApplication).appComponents
            .plus(ActivityModule(this@BaseActivity))
        injectComponents(activityComponents!!)
    }


    abstract fun injectComponents(activityComponents: ActivityComponents)


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragmentsList = supportFragmentManager.fragments
        for (i in fragmentsList.size - 1 downTo 0) {
            val fragment: Fragment? = fragmentsList[i]
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

}

fun FragmentActivity.reloadData(tag: String) {
    ((supportFragmentManager.findFragmentByTag(tag)) as? BaseFragment<*>)?.mainRequest()
}
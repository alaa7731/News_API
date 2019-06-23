package com.android.news.mvp.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.news.R
import com.android.news.dagger.components.ActivityComponents
import com.android.news.mvp.ui.BaseActivity
import com.android.news.mvp.ui.fragments.MainFragment

class MainActivity : BaseActivity() {
    override fun injectComponents(activityComponents: ActivityComponents) {
        activityComponents.inject(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.content, MainFragment())
                .commit()
        }
    }
}

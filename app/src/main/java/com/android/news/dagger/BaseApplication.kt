package com.android.news.dagger

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.android.news.dagger.components.AppComponents
import com.android.news.dagger.modules.AppModule

/**
 * This is the customize application class which will be run on the
 * init of the application process before any app component's running
 */
abstract class BaseApplication : Application() {

    // build the dependency graph
    val appComponents: AppComponents by lazy {
        @Suppress("DEPRECATION")
        DaggerAppComponents.builder().appModule(AppModule(this@BaseApplication))
                .build()
    }


    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

     }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    /**
     * Holding the reference to application context in order to use
     * it any where even out side app components context..
     */
    companion object {
        var appContext: Context? = null
    }

}

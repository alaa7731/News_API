package com.android.news.dagger.components

import com.android.news.mvp.ui.activities.MainActivity
import dagger.Subcomponent
import com.android.news.dagger.modules.ActivityModule
import com.android.news.dagger.modules.FragModule
import com.android.news.dagger.scopes.Activity

@Activity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponents {
    fun inject(activity: MainActivity)


    fun plus(fragModule: FragModule): FragComponents?
}
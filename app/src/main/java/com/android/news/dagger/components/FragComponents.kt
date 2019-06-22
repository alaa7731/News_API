package com.android.news.dagger.components

import dagger.Subcomponent
import com.android.news.dagger.modules.FragModule
import com.android.news.dagger.scopes.Fragment


@Fragment
@Subcomponent(modules = arrayOf(FragModule::class))
interface FragComponents {
//    fun inject(fragment: MainFragment)
}
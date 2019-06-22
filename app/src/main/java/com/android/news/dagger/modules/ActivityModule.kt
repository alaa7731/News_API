package com.android.news.dagger.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import com.android.news.dagger.scopes.Activity

@Module
class ActivityModule(val context: Context) {
    @Activity
    @Provides
    fun provideContext(): Context = context
}
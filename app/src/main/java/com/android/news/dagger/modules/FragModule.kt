package com.android.news.dagger.modules

import dagger.Module
import dagger.Provides
import com.android.news.dagger.scopes.Fragment
import com.android.news.mvp.presenter.BasePresenterListener

@Module
class FragModule(val viewListener: BasePresenterListener) {
    @Fragment
    @Provides
    fun providePresenterListener(): BasePresenterListener = viewListener
}
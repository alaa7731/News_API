package com.android.news.mvp.presenter

import android.support.annotation.StringRes
import android.view.View

interface BasePresenterListener {
    fun showProgress()
    fun hideProgress()
    fun showMessage(message: String)
    fun showSuccessMessage(message: String)
    fun showErrorMessage(message: String)
    fun showWarningMessage(message: String)
    fun showMessageListener(message: String ,listener: View.OnClickListener)
    fun showRetrySnackBar(@StringRes messageId: Int)
    fun showErrorSnackBar(@StringRes message: String)
}
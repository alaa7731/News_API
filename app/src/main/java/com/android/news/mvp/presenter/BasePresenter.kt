package com.android.news.mvp.presenter

import android.content.Context
import android.net.Uri
import com.android.news.R
import com.android.news.data.models.BaseResponseModel
import com.android.news.mvp.controller.BaseControllerListener
import java.io.File

abstract class BasePresenter<T : BasePresenterContract, E : BasePresenterListener>
    (val context: Context) : BaseControllerListener {
    var controller: T? = null
    var listener: E? = null
    var apiKey = "d88af0b069d147c689da502de7be2e50"
    val tag: String by lazy {
        "${javaClass.simpleName}_${Integer.toHexString(this.hashCode())}"
    }

    fun getResult(it: BaseResponseModel): String = it.status!!


    override fun onFailure(message: String?) {
        listener?.hideProgress()
        if (message != null)
            listener?.showErrorMessage(message!!)
    }

    override fun onNoInternetConnection() {
        listener?.hideProgress()
        listener?.showRetrySnackBar(R.string.alert_no_internet_connection)
    }

    override fun onConnectionFailure() {
        listener?.hideProgress()
        listener?.showErrorMessage("Connection Failure")
    }

    /**
     * cancel all currently running apis as we don't need
     * it anymore.. (to limit resources)
     */
    fun destroy() {
        listener = null

        controller?.destroy()

        val fields = this.javaClass.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                if (field.get(this) is BasePresenterContract) {
                    val controller = field.get(this) as BasePresenterContract
                    controller?.destroy()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

}
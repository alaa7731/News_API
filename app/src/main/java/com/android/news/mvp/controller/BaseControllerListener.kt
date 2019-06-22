package com.android.news.mvp.controller

interface BaseControllerListener{
    fun onNoInternetConnection()
    fun onConnectionFailure()
    fun onFailure(message: String?)
}
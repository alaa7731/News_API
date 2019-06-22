package com.android.news.network.queue;

import android.support.annotation.NonNull;

import retrofit2.Call;

/**
 * Created by Alaa on 02/08/17.
 */

public class RetrofitRequest {
    private final Call mRequestCall;
    private final RetrofitCallback mCallback;
    private boolean isRunning;

    public RetrofitRequest(@NonNull Call request,
                           @NonNull RetrofitCallback callback) {
        this.mRequestCall = request;
        this.mCallback = callback;
        isRunning = false;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @NonNull
    public Call getRequestCall() {
        return mRequestCall;
    }

    @NonNull
    public RetrofitCallback getCallback() {
        return mCallback;
    }

    public boolean isRunning() {
        return isRunning;
    }
}

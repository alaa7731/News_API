package com.android.news.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Alaa on 11/8/2017.
 */

public class SessionManagement {

    // Shared Preferences
    SecurePreferencesManagement pref;

    // Editor for Shared preferences
    SecurePreferencesManagement.Editor editor;

    // Context
    Context _context;

    private static final String TAG = "SessionManagement";

    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = new SecurePreferencesManagement(context);
        editor = pref.edit();
    }

}

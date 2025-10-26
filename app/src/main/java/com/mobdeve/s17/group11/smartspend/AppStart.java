package com.mobdeve.s17.group11.smartspend;

import android.app.Application;
import android.content.res.ColorStateList;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

public class AppStart extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

}

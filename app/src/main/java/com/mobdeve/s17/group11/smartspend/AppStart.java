package com.mobdeve.s17.group11.smartspend;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.mobdeve.s17.group11.smartspend.util.Algorithm;

public class AppStart extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Algorithm.init();
    }

}

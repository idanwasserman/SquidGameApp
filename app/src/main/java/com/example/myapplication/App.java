package com.example.myapplication;

import android.app.Application;

import com.example.myapplication.objects.MySharedPreferences;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MySharedPreferences.init(this);
    }
}

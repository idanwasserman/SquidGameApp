package com.example.myapplication.objects;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    private static MySharedPreferences instance;
    private final String SP_FILE_NAME = "SP_FILE";
    private final SharedPreferences sharedPreferences;

    public static MySharedPreferences getInstance() {
        return instance;
    }

    private MySharedPreferences(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SP_FILE_NAME,Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new MySharedPreferences(context);
        }
    }

    public double getDouble(String KEY, double defaultValue) {
        return Double.parseDouble(getString(KEY, String.valueOf(defaultValue)));
    }

    public void putDouble(String KEY, double value) {
        putString(KEY, String.valueOf(value));
    }

    public int getInt(String KEY, int defaultValue) {
        return sharedPreferences.getInt(KEY, defaultValue);
    }

    public void putInt(String KEY, int value) {
        sharedPreferences.edit().putInt(KEY, value).apply();
    }

    public String getString(String KEY, String defaultValue) {
        return sharedPreferences.getString(KEY, defaultValue);
    }

    public void putString(String KEY, String value) {
        sharedPreferences.edit().putString(KEY, value).apply();
    }
}

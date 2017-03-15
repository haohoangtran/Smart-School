package com.example.quanla.smartschool;

import android.app.Application;
import android.util.Log;

import com.example.quanla.smartschool.database.model.Checkin;
import com.example.quanla.smartschool.sharePrefs.SharedPrefs;

/**
 * Created by QuanLA on 3/10/2017.
 */

public class appSS extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefs.init(this);
    }
}

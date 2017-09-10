package com.example.margin.demo;

import android.app.Application;

import com.example.margin.demo.net.Net;

/**
 * Created by margin on 17-9-9.
 */

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Net.init(getApplicationContext());
    }
}

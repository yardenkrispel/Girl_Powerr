package com.example.Girl_Power;

import android.app.Application;
import android.util.Log;

import com.example.Girl_Power.Model.Record;
import com.example.Girl_Power.Utilities.RecordsUtil;

import java.util.ArrayList;
import java.util.Collections;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RecordsUtil.init(this);
    }
}

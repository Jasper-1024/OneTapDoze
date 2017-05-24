package com.jasperhale.onetapdoze.BaseClass;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.jasperhale.onetapdoze.MyEventBusIndex;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ZHANG on 2017/5/14.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
        context = getApplicationContext();
        super.onCreate();
    }
    public static Context getContext(){
        return context;
    }
}
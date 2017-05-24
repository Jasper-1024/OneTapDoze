package com.jasperhale.onetapdoze.Service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.jasperhale.onetapdoze.BaseClass.BaseService;
import com.jasperhale.onetapdoze.BaseClass.LogUtil;
import com.jasperhale.onetapdoze.BaseClass.MyApplication;
import com.jasperhale.onetapdoze.Model.EventBus_data;
import com.jasperhale.onetapdoze.R;
import com.jasperhale.onetapdoze.View.IdleAppWidget;

import org.greenrobot.eventbus.EventBus;

public class BroadcastService extends BaseService {
    //PowerManager
    private PowerManager powerManager;

    private IntentFilter intentFilter;
    //idlemode改变
    private IdleModeChange idlemodechage;
    //屏幕开关
    private SceenReceiver sceenReceiver;
    //更新值
    private String doze;


    public BroadcastService() {
    }


    @Override
    public void onCreate() {
        //获取实例PowerManager
        powerManager = (PowerManager) MyApplication.getContext().getSystemService(Context.POWER_SERVICE);

        //注册idlemode改变广播
        intentFilter = new IntentFilter();
        intentFilter.addAction(powerManager.ACTION_DEVICE_IDLE_MODE_CHANGED);
        idlemodechage = new IdleModeChange();
        registerReceiver(idlemodechage, intentFilter);

        //开关屏
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        sceenReceiver = new SceenReceiver();
        registerReceiver(sceenReceiver, intentFilter);

        super.onCreate();
    }

    class IdleModeChange extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (powerManager.isDeviceIdleMode()){
                doze = "ON";
            }else {
                doze = "OFF";
            }
            //更新小部件
            RemoteViews view = new RemoteViews(getPackageName(), R.layout.idle_app_widget);
            view.setTextViewText(R.id.WeightDozeMode,doze);
            ComponentName theWidget = new ComponentName(MyApplication.getContext(),IdleAppWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(MyApplication.getContext());
            manager.updateAppWidget(theWidget, view);


            LogUtil.d("Broadcast", "IdleModeChange");
            EventBus_data.IdleModeChange idleModeChange = new EventBus_data.IdleModeChange();
            idleModeChange.flag = true;
            EventBus.getDefault().post(idleModeChange);
        }
    }

    class SceenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            EventBus_data.SceenReceiver sceenReceiver = new EventBus_data.SceenReceiver();
            //传递亮屏关屏
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                sceenReceiver.flag = false;
            }else if (Intent.ACTION_SCREEN_ON.equals(action)){
                sceenReceiver.flag = true;
            }
            EventBus.getDefault().post(sceenReceiver);
            LogUtil.d("Broadcast", "sceenReceiver");
        }
    }

}

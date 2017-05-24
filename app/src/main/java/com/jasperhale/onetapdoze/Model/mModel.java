package com.jasperhale.onetapdoze.Model;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;

import com.jasperhale.onetapdoze.BaseClass.LogUtil;
import com.jasperhale.onetapdoze.BaseClass.MyApplication;

import org.greenrobot.eventbus.EventBus;

import static android.content.Context.MODE_PRIVATE;

public class mModel implements Model {

    private PowerManager powermanger;
    //键值
    private SharedPreferences IdleMod;  //应用内idle模式设置
    private SharedPreferences.Editor editor;


    //构造函数
    public mModel(){
        powermanger = (PowerManager) MyApplication.getContext().getSystemService(Context.POWER_SERVICE);

        IdleMod = MyApplication.getContext().getSharedPreferences("IdleMod", MODE_PRIVATE);
        editor = IdleMod.edit();
        editor.putBoolean("IdleModFlag", false);
        editor.putBoolean("IdleModSet", true);
        editor.putBoolean("IdleModeScreenOFF", false);
        editor.putBoolean("IdleModeScreenON", false);
        editor.commit();
    }
    //进入idle模式
    @Override
    public void EnterIdleMode() {
        EventBus_data.EnterIdleMode enterIdleMode = new EventBus_data.EnterIdleMode();
        enterIdleMode.flag = true;
        EventBus.getDefault().post(enterIdleMode);
        LogUtil.d("Model","EnterIdleMode");
    }
    //退出idle模式
    @Override
    public void ExitIdleMode() {
        EventBus_data.ExitIdleMode exitIdleMode = new EventBus_data.ExitIdleMode();
        exitIdleMode.flag = true;
        EventBus.getDefault().post(exitIdleMode);
        LogUtil.d("Model","ExitIdleMode");
    }
    //设置IdleMode
    @Override
    public void SetIdleMode(boolean flag) {
        if (flag){
            this.EnterIdleMode();
        }else if (!flag){
            this.ExitIdleMode();
        }
    }


    //返回IdleMode状态
    @Override
    public boolean CheckIdleMode() {
        return powermanger.isDeviceIdleMode();
    }
    //返回屏幕状态
    @Override
    public boolean CheckScreen() {
        return powermanger.isScreenOn();
    }

    //添加进入前时间
    @Override
    public void Settime_befor() {
        LogUtil.d("Model","Settime_befor");
        EventBus_data.Notifytime notifytime = new EventBus_data.Notifytime();
        notifytime.flag = true;
        EventBus.getDefault().post(notifytime);
    }

    //添加进入后时间
    @Override
    public void Settime_after() {
        LogUtil.d("Model","Settime_after");
        EventBus_data.Notifytime notifytime = new EventBus_data.Notifytime();
        notifytime.flag = false;
        EventBus.getDefault().post(notifytime);
    }
    //获取设置键值
    @Override
    public boolean GetPreferences(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return  prefs.getBoolean(key, false);
    }

    //写入普通键值
    @Override
    public void SetSharedPreferences(String key, boolean value) {
        editor.putBoolean(key,value).commit();
    }
    //获取普通键值
    @Override
    public boolean GetSharedPreferences(String key) {
        return IdleMod.getBoolean(key,false);
    }
}

package com.jasperhale.onetapdoze.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;

import com.jasperhale.onetapdoze.BaseClass.LogUtil;
import com.jasperhale.onetapdoze.BaseClass.MyApplication;
import com.jasperhale.onetapdoze.Model.EventBus_data;
import com.jasperhale.onetapdoze.Service.DozeService;

import org.greenrobot.eventbus.EventBus;

import static android.content.Context.MODE_PRIVATE;

public class IdleModeReceiver extends BroadcastReceiver {

    private String  IdleMode;
    private String Tag = "IdleModeReceiver";
    private PowerManager powermanger;

    //编辑键值
    private SharedPreferences IdleMod;
    private SharedPreferences.Editor editor;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        // throw new UnsupportedOperationException("Not yet implemented");
        IdleMod = MyApplication.getContext().getSharedPreferences("IdleMod", MODE_PRIVATE);
        editor = IdleMod.edit();
        powermanger =  (PowerManager) MyApplication.getContext().getSystemService(Context.POWER_SERVICE);

        //取intent附带信息
        IdleMode = intent.getStringExtra("Idle");

        LogUtil.d(Tag, IdleMode);
        //进入idle
        if (IdleMode == "true") {
            editor.putBoolean("IdleModSet", true).commit();
            EnterIdleMode();
        }
        //退出idle
        if (IdleMode.equals("false") ) {
            editor.putBoolean("IdleModSet", false).commit();
            ExitIdleMode();
        }
        //改变idle状态
        if (IdleMode.equals("change")) {
           if (powermanger.isDeviceIdleMode()){
               editor.putBoolean("IdleModSet", false).commit();
               ExitIdleMode();
           }else {
               editor.putBoolean("IdleModSet", true).commit();
               EnterIdleMode();
           }
        }
    }

    //进入idle模式
    public void EnterIdleMode() {
        EventBus_data.EnterIdleMode enterIdleMode = new EventBus_data.EnterIdleMode();
        enterIdleMode.flag = true;
        EventBus.getDefault().post(enterIdleMode);
        LogUtil.d(Tag,"EnterIdleMode");
    }
    //退出idle模式
    public void ExitIdleMode() {
        EventBus_data.ExitIdleMode exitIdleMode = new EventBus_data.ExitIdleMode();
        exitIdleMode.flag = true;
        EventBus.getDefault().post(exitIdleMode);
        LogUtil.d(Tag,"ExitIdleMode");
    }

}

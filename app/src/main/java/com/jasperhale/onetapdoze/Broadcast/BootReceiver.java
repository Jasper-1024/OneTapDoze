package com.jasperhale.onetapdoze.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;

import com.jasperhale.onetapdoze.BaseClass.LogUtil;
import com.jasperhale.onetapdoze.BaseClass.MyApplication;
import com.jasperhale.onetapdoze.Service.BroadcastService;
import com.jasperhale.onetapdoze.Service.DozeService;
import com.jasperhale.onetapdoze.Service.TimeService;
import com.jasperhale.onetapdoze.View.MainActivity;
import com.jasperhale.onetapdoze.lib.ShellUtils;

import static android.content.Context.MODE_PRIVATE;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        context.startService(new Intent(context, DozeService.class));
        context.startService(new Intent(context, BroadcastService.class));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        if (prefs.getBoolean("InterruptIdle", false)) {
            context.startService(new Intent(context, TimeService.class));
        }
        SharedPreferences IdleMod;
        IdleMod = MyApplication.getContext().getSharedPreferences("IdleMod", MODE_PRIVATE);

        LogUtil.d("Boot", "BootReceiver");

        if (IdleMod.getBoolean("IdleModSet", false)) {
            LogUtil.d("Boot", "true");

            ShellUtils.execCommand("dumpsys deviceidle force-idle", true, false);
        } else if (IdleMod.getBoolean("IdleModSet", false)) {
            LogUtil.d("Boot", "false");

            ShellUtils.execCommand("dumpsys deviceidle step", true, false);
        }
    }
}

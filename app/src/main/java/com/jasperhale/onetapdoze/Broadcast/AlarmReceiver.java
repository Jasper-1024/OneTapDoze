package com.jasperhale.onetapdoze.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jasperhale.onetapdoze.Service.TimeService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, TimeService.class);
        context.startService(i);
    }
}

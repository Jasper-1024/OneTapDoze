package com.jasperhale.onetapdoze.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.jasperhale.onetapdoze.BaseClass.BaseService;
import com.jasperhale.onetapdoze.BaseClass.LogUtil;
import com.jasperhale.onetapdoze.BaseClass.MyApplication;
import com.jasperhale.onetapdoze.Broadcast.AlarmReceiver;
import com.jasperhale.onetapdoze.Model.EventBus_data;

import org.greenrobot.eventbus.EventBus;

public class TimeService extends BaseService {

    private static EventBus_data.Heart heart;
    private PowerManager powerManager;

    public TimeService() {
    }

    @Override
    public void onCreate() {
        heart = new EventBus_data.Heart();
        powerManager = (PowerManager) MyApplication.getContext().getSystemService(Context.POWER_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (powerManager.isScreenOn()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
            if (prefs.getBoolean("InterruptIdle", false)) {
                LogUtil.d("TimeService", "on");
                //发送心跳包
                //heartbeat.flag = !(heartbeat.flag);
                EventBus.getDefault().post(heart);

                //每60s执行一次
                LogUtil.d("TimeService", "on");


                AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                int second = 60 * 1000;//15秒
                long triggerAtTimer = SystemClock.elapsedRealtime() + second;
                Intent intent1 = new Intent(this, TimeService.class);
                PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent1, 0);
                manager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTimer, pendingIntent);


                /*
                AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                int offset= 60 * 1000;//间隔时间10s
                long triggerAtTime = SystemClock.elapsedRealtime() + offset;
                Intent i = new Intent(this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);

                manager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
//              manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
*/
           } else {
              LogUtil.d("TimeService", "off");
                this.stopSelf();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}

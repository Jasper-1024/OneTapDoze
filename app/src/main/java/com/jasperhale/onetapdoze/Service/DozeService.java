package com.jasperhale.onetapdoze.Service;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.jasperhale.onetapdoze.BaseClass.BaseService;
import com.jasperhale.onetapdoze.BaseClass.LogUtil;
import com.jasperhale.onetapdoze.BaseClass.MyApplication;
import com.jasperhale.onetapdoze.Model.EventBus_data;
import com.jasperhale.onetapdoze.R;
import com.jasperhale.onetapdoze.lib.ShellUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DozeService extends BaseService {

    private NotificationManager notuficationmanger;
    private NotificationCompat.Builder build;
    private Notification notification;
    private java.util.Date time_after = null;
    private java.util.Date time_befor = null;
    private SimpleDateFormat scanf = new SimpleDateFormat("HH:mm:ss");
    private String string = "";
    ArrayList list = new ArrayList();



    //构造函数
    public DozeService() {
    }

    @Override
    public void onCreate() {
        //通知相关
        notuficationmanger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //EventBus总线注册
        EventBus.getDefault().register(this);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //EventBus总线注销
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    //进入idle模式             后台线程            优先级5          粘性事件
    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 5, sticky = true)
    public void onEvent(EventBus_data.EnterIdleMode enterIdleMode) {
        LogUtil.d("DozeService", "onEvent:" + "enterIdleMode");
        ShellUtils.execCommand("dumpsys deviceidle force-idle", true, false);
    }

    //退出idle模式             后台线程            优先级5         粘性事件
    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 5, sticky = true)
    public void onEvent(EventBus_data.ExitIdleMode exitIdleMode) {
        LogUtil.d("DozeService", "onEvent:" + "exitIdleMode");
        ShellUtils.execCommand("dumpsys deviceidle step", true, false);
    }

    //记录idle模式改变时间             UI线程            优先级3        非粘性事件
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 4, sticky = false)
    public void onEvent(EventBus_data.Notifytime notifytime) {
        LogUtil.d("DozeService", "onEvent:" + "notifytime");

        build = new NotificationCompat.Builder(this);
        long size;
        //启用通知
        boolean Notify_Flag;
        //通知优先级？
        boolean Notify_Priority;
        //通知优先级？
        int NotifyPriority = Notification.PRIORITY_MIN;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Notify_Flag = prefs.getBoolean("Notify", false);
        Notify_Priority = prefs.getBoolean("NotifyPriority", false);

        if (Notify_Priority){
            //高优先级
            NotifyPriority = Notification.PRIORITY_HIGH;
        }else {
            NotifyPriority = Notification.PRIORITY_MIN;
        }


        if (notifytime.flag) {
            time_befor = new java.util.Date(System.currentTimeMillis());
            if (Notify_Flag) {
                notification = build
                        .setContentTitle("Doze Mode")
                        .setContentText(string)
                        .setShowWhen(true)
                        .setUsesChronometer(true)
                        .setSmallIcon(R.drawable.notification)
                        .build();
                notuficationmanger.notify(5, notification);
            } else {
                //没有启用通知栏，则提示Toast且取消掉通知栏通知
                Toast.makeText(MyApplication.getContext(), "Enter Doze Success", Toast.LENGTH_SHORT).show();
                list.clear();
                notuficationmanger.cancel(5);
            }
        } else {
            if (time_befor != null) {
                time_after = new java.util.Date(System.currentTimeMillis());
                if (Notify_Flag) {
                    //默认传入秒
                    size = (time_after.getTime() - time_befor.getTime());
                    string = scanf.format(time_befor) + "-" + scanf.format(time_after) + "="
                            + trform(size);
                    list.add(string);

                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                    for (int i=list.size()-1;i>=0;i--){
                        inboxStyle.addLine(list.get(i).toString());
                    }
                    notification = build
                            .setContentTitle("Doze Mode")
                            .setContentText(string)
                            .setStyle(inboxStyle)
                            .setPriority(NotifyPriority)
                            .setSmallIcon(R.drawable.notification)
                            .build();
                    notuficationmanger.notify(5, notification);

                } else {
                    //没有启用通知栏，则提示Toast且取消掉通知栏通知
                    Toast.makeText(MyApplication.getContext(), "Exit Doze", Toast.LENGTH_SHORT).show();
                    list = new ArrayList();
                    list.clear();
                    notuficationmanger.cancel(5);
                }
            }
        }
    }

    //发送通知             UI线程                   优先级3       非粘性事件
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 4, sticky = false)
    public void onEvent(EventBus_data.Notify notify) {
        LogUtil.d("DozeService", "onEvent:" + "Notify");
        /*
        if (notify.flag) {
            notification = build
                    .setContentTitle("Doze Mode")
                    .setContentText(string)
                    .setShowWhen(true)
                    .setUsesChronometer(true)
                    .setStyle(inboxStyle)
                    .setSmallIcon(R.drawable.notification)
                    .build();
            notuficationmanger.notify(5, notification);
        } else {
            //取消通知
            inboxStyle = new NotificationCompat.InboxStyle();
            notuficationmanger.cancel(5);
            Toast.makeText(this, "Doze模式已改变", Toast.LENGTH_SHORT).show();
        }*/
       /* notification = new NotificationCompat.Builder(this)
                .setContentTitle("Doze Mode")
                .setContentText(string)
                .setShowWhen(true)
                .setUsesChronometer(true)
                //这里使用的是应用图标，一般没人这么干，就是为了方便
                .setSmallIcon(R.drawable.notification)
                .build();
        notuficationmanger.notify(5, notification);*/
    }








    String trform(long size) {
        long day, hour, min, secone;

        day = size / (1000 * 60 * 60 * 24);          //以天数为单位取整
        hour = (size / (60 * 60 * 1000) - day * 24);             //以小时为单位取整
        min = ((size / (60 * 1000)) - day * 24 * 60 - hour * 60);    //以分钟为单位取整
        secone = (size / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        if (day != 0) {
            return day + "d" + hour + "h" + min + "m" + secone + "s";
        } else if (hour != 0) {
            return hour + "h" + min + "m" + secone + "s";
        } else if (min != 0) {
            return min + "m" + secone + "s";
        } else {
            return secone + "s";
        }
    }


}

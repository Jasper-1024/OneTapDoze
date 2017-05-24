package com.jasperhale.onetapdoze.View;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.jasperhale.onetapdoze.BaseClass.LogUtil;
import com.jasperhale.onetapdoze.BaseClass.MyApplication;
import com.jasperhale.onetapdoze.R;
import com.jasperhale.onetapdoze.Service.DozeService;

/**
 * Implementation of App Widget functionality.
 */
public class IdleAppWidget extends AppWidgetProvider {
    private static final String MyOnClick = "WeightOnClickTag";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent();
        intent.setAction(MyOnClick);

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.idle_app_widget);

        views.setOnClickPendingIntent(R.id.WeightDozeMode, PendingIntent.getBroadcast(context,0,intent,0));
        views.setTextViewText(R.id.WeightDozeMode, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // 第一次创建时调用
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (MyOnClick.equals(intent.getAction())) {
            LogUtil.d("weight", "onClick");

            Intent intents = new Intent("com.jasperhale.onetapdoze.idlereceiver" );
            intents.putExtra("Idle","change");
            context.sendBroadcast(intents);
        }
    }
}


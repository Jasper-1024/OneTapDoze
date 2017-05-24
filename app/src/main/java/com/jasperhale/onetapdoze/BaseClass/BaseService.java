package com.jasperhale.onetapdoze.BaseClass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BaseService extends Service {
    public BaseService() {
    }

    @Override
    public void onCreate() {
        LogUtil.d("Service", getClass().getSimpleName()+"onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        LogUtil.d("Service", getClass().getSimpleName()+"onCreate");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

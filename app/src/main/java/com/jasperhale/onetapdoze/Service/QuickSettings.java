package com.jasperhale.onetapdoze.Service;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.PowerManager;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;
import com.jasperhale.onetapdoze.BaseClass.LogUtil;
import com.jasperhale.onetapdoze.BaseClass.MyApplication;
import com.jasperhale.onetapdoze.R;

import static android.service.quicksettings.Tile.STATE_ACTIVE;
import static android.service.quicksettings.Tile.STATE_INACTIVE;

@RequiresApi(api = Build.VERSION_CODES.N)
public class QuickSettings  extends TileService {
    //powermanger
    private PowerManager powerManager;
    private  Icon icon;
    private final String LOG_TAG = "QuickSettingService";

    //当用户从Edit栏添加到快速设定中调用
    @Override
    public void onTileAdded() {
        LogUtil.d("QuickSetting", "onTileAdded");
        icon = Icon.createWithResource(getApplicationContext(), R.drawable.notification);
        powerManager =  (PowerManager) MyApplication.getContext().getSystemService(Context.POWER_SERVICE);

        if (powerManager.isDeviceIdleMode()){
            getQsTile().setState(STATE_ACTIVE);//更改成活跃状态
        }else {
            getQsTile().setState(STATE_INACTIVE);// 更改成非活跃状态
        }
        getQsTile().setIcon(icon);//设置图标
        getQsTile().updateTile();//更新Tile
    }
    //当用户从快速设定栏中移除的时候调用
    @Override
    public void onTileRemoved() {
        LogUtil.d("QuickSetting", "onTileRemoved");
    }
    // 点击的时候
    @Override
    public void onClick() {
        LogUtil.d(LOG_TAG, "onClick state = " + Integer.toString(getQsTile().getState()));

        //更改idle模式状态
        Intent intents = new Intent("com.jasperhale.onetapdoze.idlereceiver" );
        intents.putExtra("Idle","change");
        this.sendBroadcast(intents);

        if (getQsTile().getState() == STATE_ACTIVE){
            getQsTile().setState(STATE_INACTIVE);// 更改成非活跃状态
        }
        if (getQsTile().getState() == STATE_INACTIVE){
            getQsTile().setState(STATE_ACTIVE);// 更改成非活跃状态
        }

        getQsTile().setIcon(icon);//设置图标
        getQsTile().updateTile();//更新Tile
    }
    // 打开下拉菜单的时候调用,当快速设置按钮并没有在编辑栏拖到设置栏中不会调用
    //在TleAdded之后会调用一次
    @Override
    public void onStartListening () {
        LogUtil.d("QuickSetting", "onStartListening");
        if (powerManager.isDeviceIdleMode()){
            getQsTile().setState(STATE_ACTIVE);//更改成活跃状态
        }else {
            getQsTile().setState(STATE_INACTIVE);// 更改成非活跃状态
        }
        getQsTile().updateTile();//更新Tile
    }
    // 关闭下拉菜单的时候调用,当快速设置按钮并没有在编辑栏拖到设置栏中不会调用
    // 在onTileRemoved移除之前也会调用移除
    @Override
    public void onStopListening () {
        LogUtil.d("QuickSetting", "onStopListening");
    }

}
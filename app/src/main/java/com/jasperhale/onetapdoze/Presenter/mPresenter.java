package com.jasperhale.onetapdoze.Presenter;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jasperhale.onetapdoze.BaseClass.BasePresenter;
import com.jasperhale.onetapdoze.BaseClass.LogUtil;
import com.jasperhale.onetapdoze.BaseClass.MyApplication;
import com.jasperhale.onetapdoze.Model.EventBus_data;
import com.jasperhale.onetapdoze.Model.Model;
import com.jasperhale.onetapdoze.Model.mModel;
import com.jasperhale.onetapdoze.View.Viewif;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ZHANG on 2017/3/25.
 */

public class mPresenter extends BasePresenter<Viewif> implements Presenter {
    private Model Model;

    public mPresenter(Viewif view) {
        this.attach(view);
        this.Model = new mModel();
        //注册EventBus总线
        EventBus.getDefault().register(this);
    }

    //进入Idle模式
    @Override
    public void EnterIdleMode() {
        //写入IdleModSet键值
        Model.SetSharedPreferences("IdleModSet", true);
        //进入idle模式
        Model.EnterIdleMode();
    }

    //退出Idle模式
    @Override
    public void ExitIdleMode() {
        //写入IdleModSet键值
        Model.SetSharedPreferences("IdleModSet", false);
        //退出idle模式
        Model.ExitIdleMode();
    }

    @Override
    public void IdleModeSetInit() {
        Model.SetIdleMode(Model.GetSharedPreferences("IdleModSet"));
    }

    //更新idle模式指示
    @Override
    public void UpdateUIIdleMode() {
        mView.UpdateUIIdleModeFlag(Model.CheckIdleMode());
    }

    @Override
    public void UpTimeService() {
        mView.UpdateTimeService(Model.GetPreferences("InterruptIdle"));
    }

    //idle模式改变          UI线程                优先级4       非粘性事件
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 4, sticky = false)
    public void onEvent(EventBus_data.IdleModeChange idleModeChange) {
        LogUtil.d("Presenter", "IdleModeChange");
        //设置Idle标志位
        //Model.SetSharedPreferences("IdleModFlag", Model.CheckIdleMode());

        //更新idle模式指示
        if (!(mView==null)){
            this.UpdateUIIdleMode();
        }
        //记录idle模式改变时间
        if (Model.CheckIdleMode()) {
            Model.Settime_befor();
        } else if (!Model.CheckIdleMode()) {
            Model.Settime_after();
        }

        if (Model.CheckScreen()) {
            //保持Doze，亮屏有效
            if (Model.GetPreferences("KeepIdle")) {
                //设置Idle模式为IdleModSet值
                Model.SetIdleMode(Model.GetSharedPreferences("IdleModSet"));
            }
        }

    }

    //屏幕开关              后台线程                 优先级4       非粘性事件
    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 4, sticky = false)
    public void onEvent(EventBus_data.SceenReceiver sceenReceiver) {
        LogUtil.d("Presenter", "Sceen" + String.valueOf(sceenReceiver.flag));
        //时间更新服务
        this.UpTimeService();

        if (sceenReceiver.flag) {
            //写入屏幕开启前idle模式状态
            Model.SetSharedPreferences("IdleModeScreenOFF", Model.CheckIdleMode());
        }else {
            //写入屏幕关闭前idle模式状态
            Model.SetSharedPreferences("IdleModeScreenON", Model.CheckIdleMode());
        }
        //仅亮屏模式
        if (Model.GetPreferences("JustSceenOn")){
            //开屏
            if (sceenReceiver.flag){
                //设置上一次开屏值
                Model.SetIdleMode(Model.GetSharedPreferences("IdleModeScreenON"));
            }//关屏
            else if (!sceenReceiver.flag){
                //设置上一次关屏值
                Model.SetIdleMode(Model.GetSharedPreferences("IdleModeScreenOFF"));
            }
        }
    }

    //屏幕开关              后台线程                 优先级4       非粘性事件
    @Subscribe(threadMode = ThreadMode.ASYNC, priority = 3, sticky = false)
    public void onEvent(EventBus_data.Heart heart) {
        if (Model.CheckIdleMode()){
            this.ExitIdleMode();
            //延时2s
            new Thread(new Runnable(){
                public void run(){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            this.EnterIdleMode();
        }
    }

}
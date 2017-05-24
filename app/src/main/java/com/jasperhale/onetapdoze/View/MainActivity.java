package com.jasperhale.onetapdoze.View;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jasperhale.onetapdoze.BaseClass.BaseActivity;
import com.jasperhale.onetapdoze.BaseClass.LogUtil;
import com.jasperhale.onetapdoze.Other.AboutActivity;
import com.jasperhale.onetapdoze.Other.HelpActivity;
import com.jasperhale.onetapdoze.Presenter.Presenter;
import com.jasperhale.onetapdoze.Presenter.mPresenter;
import com.jasperhale.onetapdoze.R;
import com.jasperhale.onetapdoze.Service.BroadcastService;
import com.jasperhale.onetapdoze.Service.DozeService;
import com.jasperhale.onetapdoze.Service.TimeService;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;


import java.util.Arrays;

import eu.chainfire.libsuperuser.Shell;

public class MainActivity extends BaseActivity<Viewif, mPresenter> implements Viewif, NavigationView.OnNavigationItemSelectedListener {

    private Presenter presenter;

    private Toolbar toolbar;//toolbar
    private DrawerLayout mDrawerLayout;//侧滑菜单

    private TextView Idle_Mode;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar实例初始化
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

        //汉堡菜单
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Idle_Mode = (TextView) findViewById(R.id.Idle_Mode);

        //侧滑菜单点击事件
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //presenter初始化
        presenter = (Presenter) mpresenter;

        //获取root权限
        GetRoot();
        //启用doze服务
        startService(new Intent(this, DozeService.class));
        //启用broadcast服务
        startService(new Intent(this, BroadcastService.class));

        //更新idle模式指示
        presenter.UpdateUIIdleMode();

        //时间更新服务
        presenter.UpTimeService();

        //初始idle模式
        presenter.IdleModeSetInit();

        //快捷方式
        shortcut();
    }

    //Presenter初始化
    public mPresenter initPresenter() {
        return new mPresenter(this);
    }

    // 侧滑菜单点击处理
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.whilt:
                Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                startActivity(intent);
                LogUtil.d("Navigation", "whilt");
                ;
                break;
            case R.id.setting:
                Intent intents = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intents);
                LogUtil.d("Navigation", "setting");
                ;
                break;
            case R.id.about:
                LogUtil.d("Navigation", "about");
                Intent intenta = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intenta);
                ;
                break;
            case R.id.adb:
                LogUtil.d("Navigation", "adb");
                Intent intentad = new Intent(MainActivity.this, AdbActivity.class);
                startActivity(intentad);
                ;
                break;
            case R.id.help:
                LogUtil.d("Navigation", "help");
                Intent intenth = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intenth);
                ;
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //返回按钮处理
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Activity重新刷新时
    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //EventBus总线注销
        //EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /************************************************************/
    //获取root权限
    private void GetRoot() {
        //获取root权限
        Tasks.executeInBackground(MainActivity.this, new BackgroundWork<Boolean>() {
            @Override
            public Boolean doInBackground() throws Exception {
                return Shell.SU.available();
            }

        }, new Completion<Boolean>() {
            @Override
            public void onSuccess(Context context, Boolean result) {
                if (result) {
                    Toast.makeText(context, "Get Su Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Context context, Exception e) {
                Toast.makeText(context, "Get Su Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //进入idle模式
    public void EnterDoze(View view) {
        //ShellUtils.execCommand("dumpsys deviceidle force-idle",true,false);
        presenter.EnterIdleMode();
    }

    public void ExitDoze(View view) {
        //ShellUtils.execCommand("dumpsys deviceidle step",true,false);
        mpresenter.ExitIdleMode();
    }
    //快捷方式
    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void shortcut(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            Intent intents = new Intent("com.jasperhale.onetapdoze.idlereceiver");
            intents.putExtra("Idle", "change");

            ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id1")
                    .setShortLabel("切换")
                    .setLongLabel("切换Doze模式状态")
                    .setIcon(Icon.createWithResource(this, R.drawable.notification))
                    .setIntent(intents)
                    .build();
            shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));
        }
    }



    /*******************************************************************/
    //设置当前doze状态
    @Override
    public void UpdateUIIdleModeFlag(boolean IdleModeFlag ) {
        if (IdleModeFlag) {
            Idle_Mode.setText("ON");
            Idle_Mode.setTextColor(Color.GREEN);
        }else {
            Idle_Mode.setText("OFF");
            Idle_Mode.setTextColor(Color.RED);
        }
    }
    //启用时间更新服务

    @Override
    public void UpdateTimeService(boolean flag) {
        if (flag){
            startService(new Intent(this, TimeService.class));
        }else {
            stopService(new Intent(this, TimeService.class));
        }
    }
}

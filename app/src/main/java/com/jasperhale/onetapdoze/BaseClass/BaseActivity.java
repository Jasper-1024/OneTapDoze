package com.jasperhale.onetapdoze.BaseClass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by ZHANG on 2017/3/22.
 */

public abstract class BaseActivity <Viewinterface,mPresenter extends BasePresenter<Viewinterface>>extends AppCompatActivity {
    //获取Presenter对象
    public mPresenter mpresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Presenter实例化
        mpresenter = initPresenter();
        //打印当前activity
        LogUtil.d("Activity", getClass().getSimpleName()+"onCreate");
    }

    @Override
    protected void onResume() {
        //重新刷新时重新绑定view
        mpresenter.attach((Viewinterface) this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //解绑presenter持有的view
        mpresenter.dettach();
        super.onDestroy();
        Log.d("Activity", getClass().getSimpleName()+"onDestroy");
    }


    public  abstract mPresenter initPresenter();
}

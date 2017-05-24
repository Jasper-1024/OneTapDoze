package com.jasperhale.onetapdoze.View;


import com.jasperhale.onetapdoze.BaseClass.BaseView;

/**
 * Created by ZHANG on 2017/3/26.
 */

public interface Viewif extends BaseView {
    //设置当前doze状态
    public void UpdateUIIdleModeFlag(boolean IdleModeFlag);
    //启用时间更新服务
    public void UpdateTimeService(boolean flag);
}
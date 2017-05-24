package com.jasperhale.onetapdoze.Model;



public interface Model {
    public void EnterIdleMode();
    public void ExitIdleMode();
    public void SetIdleMode(boolean flag);
    public boolean CheckIdleMode();
    //返回屏幕状态
    public boolean CheckScreen();

    //存入进入之前时间
    public void Settime_befor();
    //存入退出之前时间
    public void Settime_after();
    //获取设置键值
    public boolean GetPreferences(String key);
    //写入键值
    public void SetSharedPreferences(String key,boolean value);
    //读取键值
    public boolean GetSharedPreferences(String key);
}

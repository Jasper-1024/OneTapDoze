package com.jasperhale.onetapdoze.Model;

/**
 * Created by ZHANG on 2017/5/13.
 */

public class EventBus_data {
    public static class EnterIdleMode{
        public boolean flag;
    }
    public static class ExitIdleMode{
        public boolean flag;
    }
    //IdleMode改变
    public static class IdleModeChange{
        public boolean flag;
    }
    //亮屏灭屏
    public static class SceenReceiver{
        public boolean flag;
    }

    //时间
    public static class Notifytime{
        public boolean flag;
    }
    //发送通知
    public static class Notify{
        public boolean flag;
    }
    //心跳包
    public static class Heart{
        public boolean flag;
    }

}

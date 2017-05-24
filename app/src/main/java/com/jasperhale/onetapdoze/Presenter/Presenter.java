package com.jasperhale.onetapdoze.Presenter;

/**
 * Created by ZHANG on 2017/3/25.
 */

public interface Presenter {
    public void EnterIdleMode();
    public void ExitIdleMode();
    public void IdleModeSetInit();
    public void UpdateUIIdleMode();
    public void UpTimeService();
}

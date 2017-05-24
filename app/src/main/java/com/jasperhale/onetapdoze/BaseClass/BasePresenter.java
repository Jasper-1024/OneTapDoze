package com.jasperhale.onetapdoze.BaseClass;

/**
 * Created by ZHANG on 2017/3/22.
 */

public abstract class BasePresenter<Viewinterface> {
    //传入泛型
    public Viewinterface mView;
    //绑定
    public void attach(Viewinterface mView) {
        this.mView = mView;
    }
    //解绑，防止view为空是内存泄漏
    public void dettach() {
        mView = null;
    }
}

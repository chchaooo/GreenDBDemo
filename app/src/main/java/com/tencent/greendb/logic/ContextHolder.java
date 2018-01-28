package com.tencent.greendb.logic;

import android.content.Context;

/**
 * Created by chaochen on 2018/1/25.
 */

public class ContextHolder {

    private ContextHolder(){}

    private static class InnerHodler{
        private final static ContextHolder sInstance = new ContextHolder();
    }

    private Context mContext;

    public Context getContext() {
        return mContext;
    }

    public static ContextHolder getInstance() {
        return InnerHodler.sInstance;
    }

    public void setContext(Context context) {
        mContext = context.getApplicationContext();
    }

    public static Context getAppContext(){
        return getInstance().getContext();
    }

}

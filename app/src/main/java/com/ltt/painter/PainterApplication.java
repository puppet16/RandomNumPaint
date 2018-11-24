package com.ltt.painter;

import android.app.Application;
import android.content.Context;

import com.ltt.painter.utils.CrashHandler;

import java.lang.ref.WeakReference;


public class PainterApplication extends Application {

    public static final String TAG = "painter";
    private static WeakReference<Context> mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init();
        mContext = new WeakReference<>(getApplicationContext());

    }

    /**
     * 全局获取 Context
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        return mContext.get();
    }
}

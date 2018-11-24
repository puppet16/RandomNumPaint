package com.ltt.painter.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.ltt.painter.BuildConfig;
import com.ltt.painter.PainterApplication;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *异常消息捕获类
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private boolean mCrashed;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler mCrashHandlerInstance;

    private boolean mSDReady = false;
    private DateFormat mFormatter;

    public static synchronized CrashHandler getInstance() {
        if (null == mCrashHandlerInstance) {
            synchronized (CrashHandler.class) {
                if (null == mCrashHandlerInstance) {
                    mCrashHandlerInstance = new CrashHandler();
                }
            }
        }
        return mCrashHandlerInstance;
    }

    /**
     * 初始化捕获类
     */
    public void init() {
        mCrashed = false;
        //获取系统默认的 UncaughtExceptionHandler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置自身为进程的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);

        mSDReady = SDUtil.initLogDir();

        mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (mCrashed) {
            return;
        }
        mCrashed = true;

        // 输出异常信息
        e.printStackTrace();
        // 我们没有处理异常 并且默认异常处理不为空 则交给系统处理
        if (!handleException(e) && mDefaultHandler != null) {
            // 系统处理
            mDefaultHandler.uncaughtException(t, e);
        }

        killProcess();
    }

    private void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private boolean handleException(Throwable e) {
        if (null == e) {
            return false;
        }

        try {
            // 异常信息
            String log = getCrashLog(e);
            // 可以上传日志到后台
            // 保存到 SD 卡
            saveLogToSdcard(log);

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 获取异常信息
     *
     * @param e 异常
     * @return 异常信息字符串
     */
    private String getCrashLog(Throwable e) {
        StringBuffer log = new StringBuffer();

        //app版本信息
        log.append("App Version：")
                .append(BuildConfig.VERSION_NAME)
                .append("_")
                .append(BuildConfig.VERSION_CODE)
                .append("\n")

                //手机系统信息
                .append("OS Version：")
                .append(Build.VERSION.RELEASE)
                .append("_")
                .append(Build.VERSION.SDK_INT)
                .append("\n")

                //手机制造商
                .append("Vendor: ")
                .append(Build.MANUFACTURER)
                .append("\n")

                //手机型号
                .append("Model: ")
                .append(Build.MODEL)
                .append("\n");

        if (null != e) {
            String errorStr = TextUtils.isEmpty(e.getLocalizedMessage())
                    ? TextUtils.isEmpty(e.getMessage())
                    ? e.toString()
                    : e.getMessage()
                    : e.getLocalizedMessage();

            log.append("Exception: ")
                    .append(errorStr)
                    .append("\n");

            StackTraceElement[] elements = e.getStackTrace();
            if (null != elements) {
                for (StackTraceElement element : elements) {
                    log.append(element.toString())
                            .append("\n");
                }
            }

        } else {
            log.append("no exception. Throwable is null\n");
        }

        return log.toString();
    }

    /**
     * 保存错误日志到 SD 卡
     *
     * @param log 日志
     */
    private void saveLogToSdcard(String log) {
        try {
            Log.w(PainterApplication.TAG, "Caught an Unhandled exception, see log");
            String time = mFormatter.format(new Date());
            String fileName = "Crash-" + time + ".log";
            if (mSDReady) {
                String dir = SDUtil.getLogDir().getAbsolutePath() + "/";
                FileOutputStream fos = new FileOutputStream(dir + fileName);
                fos.write(log.getBytes());
                fos.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

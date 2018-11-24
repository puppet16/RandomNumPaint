package com.ltt.painter.utils;

import android.content.Context;
import android.widget.Toast;

import com.ltt.painter.PainterApplication;

/**
 *
 */

public class ToastUtil {
    private static Toast toast;

    public static void show(String content) {
        if (null == toast) {
            toast = Toast.makeText(PainterApplication.getContext(), content, Toast.LENGTH_SHORT);

        } else {
            toast.setText(content);
        }

        toast.show();
    }

    public static void cancel() {
        if (null != toast) {
            toast.cancel();
        }
    }
}

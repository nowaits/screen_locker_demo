
package com.demo.screen_locker.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

class CustomToast {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context mContext, String text, int duration) {

        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, duration);
        mHandler.postDelayed(r, duration);

        mToast.show();
    }

    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getResources().getString(resId), duration);
    }

}

public class ToastHelper {

    public static void showToast(final Context context, final String msg) {
        ToastHelper.showToast(context, msg, 2000);
    }

    public static void showToast(final Context context, final String msg,
            final int time) {
        ThreadUtils.postOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context, msg, time).show();
            }
        });
    }
}

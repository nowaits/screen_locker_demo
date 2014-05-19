
package com.demo.screen_locker.utils;

import java.lang.reflect.Method;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.MediaStore;

public class SysUtils {

    private static MediaPlayer sMediaPlayer = null;

    public static void Vibrate(Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context
                .getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void Vibrate(Context context, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) context
                .getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    public static void playSound(Context context) {
        Uri mediaUri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        try {
            if (sMediaPlayer != null) {
                sMediaPlayer.stop();
                sMediaPlayer.release();
                sMediaPlayer = null;
            }

            sMediaPlayer = MediaPlayer.create(context, mediaUri);

            sMediaPlayer.setLooping(false);
            sMediaPlayer.start();
        } catch (Exception e) {
        }
    }

    public static void LunchCamera(Context context) {
        String action = MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA;

        if (isKeyguardSecure(context)
                && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            action = MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE;
        }

        Intent i = new Intent(action);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static boolean isKeyguardSecure(Context c) {

        boolean is_keyguard_s = true;

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                Object statusBarManager = c.getSystemService(Context.KEYGUARD_SERVICE);
                Method method = statusBarManager.getClass().getMethod("isKeyguardSecure");
                Object ret_obj = method.invoke(statusBarManager);
                if (ret_obj instanceof Boolean) {
                    is_keyguard_s = ((Boolean) ret_obj).booleanValue();
                }               
            }
        } catch (Exception localException) {
        }
        return is_keyguard_s;
    }

    public static int killProcess() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        return pid;
    }
}

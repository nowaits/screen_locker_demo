
package com.demo.screen_locker;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.demo.screen_locker.utils.SLog;
import com.demo.screen_locker.utils.SysUtils;
import com.demo.screen_locker.utils.ToastHelper;

public class ScreenLockerInitReceiver extends BroadcastReceiver {
    public static int sCheckDuration = 5 * 60 * 1000;
    public static String sStartAction = "com.demo.ScreenLocker.locker_actions_start";
    public static String sStopAction = "com.demo.ScreenLocker.locker_actions_stop";
    public static String sCheckAction = "com.demo.ScreenLocker.locker_actions_check";

    public static void bind(Context context) {
        // context.startService(new Intent(ScreenLockerService.sAction));
        context.startService(new Intent(context, ScreenLockerService.class));
    }

    public static boolean isServiceWorked(Context context) {
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> runningService = myManager
                .getRunningServices(Integer.MAX_VALUE);
        String service_name = context.getPackageName() + "."
                + "ScreenLockerService";

        for (RunningServiceInfo info : runningService) {
            if (info.service.getClassName().toString().equals(service_name)) {
                return info.pid != 0;
            }
        }

        return false;
    }

    public static void StartService(Context context) {
        if (!isServiceWorked(context))
            context.sendBroadcast(new Intent(
                    ScreenLockerInitReceiver.sStartAction));
    }

    public static void StopService(Context context) {
        if (isServiceWorked(context))
            context.sendBroadcast(new Intent(
                    ScreenLockerInitReceiver.sStopAction));
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(sCheckAction)) {
            if (!ScreenLockerInitReceiver.isServiceWorked(context)) {
                Intent i = new Intent(ScreenLockerService.sAction);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(i);
                SysUtils.playSound(context);
                ToastHelper.showToast(context, "Service is Started !");
                SLog.d(StartActivity.sTag,
                        "InitReceiver::InitReceiver::CheckAction and Start OK !");
            } else {
                ToastHelper.showToast(context, "Service is running !");
                SLog.d(StartActivity.sTag,
                        "InitReceiver::InitReceiver::CheckAction and Running !");
            }

        } else if (intent.getAction().equals(sStopAction)) {
            AlarmManager alarm = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);

            PendingIntent s = PendingIntent.getBroadcast(context, 1,
                    new Intent(ScreenLockerInitReceiver.sCheckAction), 0);

            alarm.cancel(s);

            context.stopService(new Intent(ScreenLockerService.sAction));
            SLog.d(StartActivity.sTag,
                    "InitReceiver::InitReceiver::Service is Stoped !");
            ToastHelper.showToast(context, "Service is Stoped !");
        }
        else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)
                || intent.getAction().equals(sStartAction)) {

            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
                SLog.d(StartActivity.sTag,
                        "onReceive::InitReceiver::Intent.ACTION_BOOT_COMPLETED");
            else
                SLog.d(StartActivity.sTag,
                        "onReceive::InitReceiver::Intent.sInitAction");

            AlarmManager alarm = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);

            PendingIntent s = PendingIntent.getBroadcast(context, 1,
                    new Intent(ScreenLockerInitReceiver.sCheckAction), 0);

            alarm.cancel(s);

            alarm.setRepeating(AlarmManager.RTC, 0,
                    ScreenLockerInitReceiver.sCheckDuration,
                    s);
        }
    }
}

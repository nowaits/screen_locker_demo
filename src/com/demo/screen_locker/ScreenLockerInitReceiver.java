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

	public static PendingIntent sServiceCheckSender;

	public static int sCheckDuration = 5 * 60 * 1000;
	public static String sInitAction = "com.demo.ScreenLocker.locker_actions_init";
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
				return true;
			}
		}

		return false;
	}

	public static void CheckService(Context context) {
		if (!isServiceWorked(context))
			context.sendBroadcast(new Intent(
					ScreenLockerInitReceiver.sInitAction));
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(sCheckAction)) {
			SLog.d(StartActivity.sTag,
					"InitReceiver::InitReceiver::sCheckAction");
			if (!ScreenLockerInitReceiver.isServiceWorked(context)) {
				context.startService(new Intent(
						context.getApplicationContext(),
						ScreenLockerService.class));
				SysUtils.playSound(context);
				ToastHelper.showToast(context, "Service is Started !");
			} else {
				ToastHelper.showToast(context, "Service is running !");
			}

		} else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)
				|| intent.getAction().equals(sInitAction)) {

			if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
				SLog.d(StartActivity.sTag,
						"onReceive::InitReceiver::Intent.ACTION_BOOT_COMPLETED");
			else
				SLog.d(StartActivity.sTag,
						"onReceive::InitReceiver::Intent.sInitAction");

			AlarmManager alarm = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			if (sServiceCheckSender != null) {
				alarm.cancel(sServiceCheckSender);
				sServiceCheckSender = null;
			}

			Intent intentf = new Intent(ScreenLockerInitReceiver.sCheckAction);

			sServiceCheckSender = PendingIntent.getBroadcast(context, 1,
					intentf, 0);

			alarm.setRepeating(AlarmManager.RTC, 0,
					ScreenLockerInitReceiver.sCheckDuration,
					sServiceCheckSender);
		}
	}
}

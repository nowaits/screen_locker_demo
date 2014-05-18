package com.demo.screen_locker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.demo.screen_locker.utils.SLog;
import com.demo.screen_locker.utils.SysUtils;

public class ScreenLockerService extends Service {

	public static String sAction = "com.demo.ScreenLocker.service";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		SLog.d(StartActivity.sTag, "ScreenLockerService::onCreate");
	}

	@Override
	public void onDestroy() {
		SLog.d(StartActivity.sTag, "ScreenLockerService::onDestroy");
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		SLog.d(StartActivity.sTag, "ScreenLockerService::onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		SLog.d(StartActivity.sTag, "ScreenLockerService::onRebind");
		super.onRebind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		BroadcastReceiver mMasterResetReciever = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(Intent.ACTION_SCREEN_ON)) {
					SLog.d(StartActivity.sTag,
							"ScreenLockerService::onReceive::ACTION_SCREEN_ON");
				} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
					SLog.d(StartActivity.sTag,
							"ScreenLockerService::onReceive::ACTION_SCREEN_OFF");

					Intent i = new Intent("ScreenLockerActivity");
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					ScreenLockerService.this.startActivity(i);

				} else if (action.equals(Intent.ACTION_USER_PRESENT)) {
					SLog.d(StartActivity.sTag,
							"ScreenLockerService::onReceive::ACTION_USER_PRESENT");
					if (SysUtils.isKeyguardSecure(context)) {
						ScreenLockerActivity.CloseActivity();
					}
				}
			}
		};

		IntentFilter filter = new IntentFilter();

		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		filter.setPriority(Intent.FLAG_RECEIVER_REGISTERED_ONLY
				| Intent.FLAG_RECEIVER_REPLACE_PENDING
				| IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(mMasterResetReciever, filter);

		SLog.d(StartActivity.sTag, "ScreenLockerService::onStart");
	}

}

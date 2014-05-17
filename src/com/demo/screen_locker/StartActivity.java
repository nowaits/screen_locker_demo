package com.demo.screen_locker;

import android.app.Activity;
import android.os.Bundle;

public class StartActivity extends Activity {
	public static String sTag = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);

		ScreenLockerInitReceiver.CheckService(this);
		 finish();
	}
}

package com.demo.screen_locker.utils;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.MediaStore;

public class SysHelper {

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
		Intent i = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
	
	public static boolean isKeyguardSecure(Context c){
		KeyguardManager mgr = (KeyguardManager) c.getSystemService(Context.KEYGUARD_SERVICE);
		 
		return mgr.isKeyguardSecure();
	}

}

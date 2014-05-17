package com.demo.screen_locker;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;

import com.demo.ScreenLocker.R;
import com.demo.screen_locker.utils.SLog;
import com.demo.screen_locker.utils.TexttoSpeechUtils;

public class ScreenLockerActivity extends Activity implements
		GestureDetector.OnGestureListener, Animator.AnimatorListener,
		AnimatorUpdateListener {

	private static Activity sScreenLockerActivity;
	private View mMainView;

	private ValueAnimator mAnimator = null;

	private final BroadcastReceiver homePressReceiver = new BroadcastReceiver() {
		final String SYSTEM_DIALOG_REASON_KEY = "reason";
		final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);

				if (reason != null
						&& reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
					SLog.d(StartActivity.sTag,
							"ScreenLockerActivity.Home.Press");
				}
			}
		}
	};

	public static void CloseActivity() {
		if (sScreenLockerActivity != null
				&& !sScreenLockerActivity.isFinishing()) {
			SLog.d(StartActivity.sTag, "ScreenLockerActivity finish");
			sScreenLockerActivity.finish();
			sScreenLockerActivity = null;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sScreenLockerActivity = this;
		setContentView(R.layout.screen_locker);
		mMainView = findViewById(R.id.main_view);

		final IntentFilter homeFilter = new IntentFilter(
				Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

		homeFilter.setPriority(Intent.FLAG_RECEIVER_REGISTERED_ONLY
				| Intent.FLAG_RECEIVER_REPLACE_PENDING
				| IntentFilter.SYSTEM_HIGH_PRIORITY);

		getApplicationContext().registerReceiver(homePressReceiver, homeFilter);

		// getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

		findViewById(R.id.button).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						EditText t = (EditText) findViewById(R.id.editText2);
						String s = t.getText().toString();
						if (s != null && !s.isEmpty()) {
							TexttoSpeechUtils.SpeechText(
									ScreenLockerActivity.this
											.getApplicationContext(), s);
						}
					}
				});

		findViewById(R.id.touch_move_view).setOnTouchListener(
				new View.OnTouchListener() {

					GestureDetector mGestreDetector = new GestureDetector(
							ScreenLockerActivity.this,
							ScreenLockerActivity.this);

					@Override
					public boolean onTouch(View paramView,
							MotionEvent paramMotionEvent) {
						if (paramMotionEvent.getAction() == MotionEvent.ACTION_UP) {
							float a = mMainView.getX();
							float b = 0;

							if (mMainView.getX() >= ScreenLockerView.sMaxOffsetX) {
								b = mMainView.getRootView().getWidth();
							}

							startAnimator(a, b, ScreenLockerView.sDefaultDura);

							return false;
						}
						return mGestreDetector.onTouchEvent(paramMotionEvent);
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		SLog.d(StartActivity.sTag, "ScreenLockerActivity.onDestroy");

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return true;// super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onDown(MotionEvent paramMotionEvent) {
		return true;
	}

	@Override
	public void onShowPress(MotionEvent paramMotionEvent) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent paramMotionEvent) {

		return true;
	}

	@Override
	public boolean onScroll(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {

		mMainView.offsetLeftAndRight(-(int) paramFloat1);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent paramMotionEvent) {
	}

	@Override
	public boolean onFling(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		return false;
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		mMainView.setX(AnimatorValue());
	}

	@Override
	public void onAnimationStart(Animator animation) {

	}

	@Override
	public void onAnimationEnd(Animator animation) {
		if (mMainView.getX() > mMainView.getRootView().getWidth() / 3) {
			finish();
		}
	}

	@Override
	public void onAnimationCancel(Animator animation) {

	}

	@Override
	public void onAnimationRepeat(Animator animation) {

	}

	void startAnimator(float a, float b, long duration) {
		stopAnimator();

		mAnimator = ValueAnimator.ofFloat(a, b);
		mAnimator.addUpdateListener(this);
		mAnimator.addListener(this);

		mAnimator.setInterpolator(new DecelerateInterpolator());

		mAnimator.setDuration(duration);
		mAnimator.start();
	}

	private float AnimatorValue() {
		if (mAnimator == null) {
			return 0;
		} else {
			Float v = (Float) mAnimator.getAnimatedValue();
			return v == null ? 0.0f : v.floatValue();
		}
	}

	private void stopAnimator() {
		if (mAnimator != null && mAnimator.isRunning()) {
			mAnimator.removeAllListeners();
			mAnimator.removeAllUpdateListeners();
			mAnimator.cancel();
		}
	}
}

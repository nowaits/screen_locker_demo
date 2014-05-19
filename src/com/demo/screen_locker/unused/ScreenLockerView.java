
package com.demo.screen_locker.unused;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.demo.screen_locker.R;
import com.demo.screen_locker.StartActivity;
import com.demo.screen_locker.utils.SLog;

@SuppressWarnings("deprecation")
public class ScreenLockerView implements GestureDetector.OnGestureListener,
        Animator.AnimatorListener, AnimatorUpdateListener {
    public static int sMaxOffsetX = 300;
    public static int sMaxOffsetY = 300;
    public static int sDefaultDura = 300;
    private Context mContext = null;

    private ValueAnimator mAnimator = null;
    View mFloatLayout;
    WindowManager mWindowManager;

    private static ScreenLockerView sLocker;

    KeyguardManager mKeyguardManager;
    KeyguardLock mKeyguardLock;

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

                }
            }
        }
    };

    ScreenLockerView(Context context) {
        mContext = context;
        final IntentFilter homeFilter = new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        homeFilter.setPriority(Intent.FLAG_RECEIVER_REGISTERED_ONLY
                | Intent.FLAG_RECEIVER_REPLACE_PENDING
                | IntentFilter.SYSTEM_HIGH_PRIORITY);

        context.getApplicationContext().registerReceiver(homePressReceiver,
                homeFilter);
    }

    public static void ShowFloatView(Context context, int layout_id) {
        CloseFloatView();
        sLocker = new ScreenLockerView(context);
        sLocker.Show(layout_id);

        SLog.d(StartActivity.sTag, "ScreenLockerView::ShowFloatView");
    }

    public static void CloseFloatView() {
        if (sLocker != null) {
            SLog.d(StartActivity.sTag, "ScreenLockerView::CloseFloatView");
            sLocker.Close();
            sLocker = null;
        }
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

    private void Close() {
        mKeyguardLock.reenableKeyguard();
        mWindowManager.removeView(mFloatLayout);
    }

    private void Show(int layout_id) {

        mKeyguardManager = (KeyguardManager) mContext
                .getSystemService(Context.KEYGUARD_SERVICE);
        mKeyguardLock = mKeyguardManager.newKeyguardLock("");
        mKeyguardLock.disableKeyguard();
        ;
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        mFloatLayout = inflater.inflate(layout_id, null);

        mWindowManager.addView(mFloatLayout, getLayoutParams());
        /*
         * mFloatLayout.findViewById(R.id.text_view).setOnClickListener( new
         * View.OnClickListener() {
         * @Override public void onClick(View v) { // mFloatLayout.setX(100); //
         * ShowDialog(context); } });
         */
        mFloatLayout.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector mGestreDetector = new GestureDetector(mContext,
                    ScreenLockerView.this);

            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                if (paramMotionEvent.getAction() == MotionEvent.ACTION_UP) {
                    float a = mFloatLayout.getX();
                    float b = 0;

                    if (mFloatLayout.getX() >= ScreenLockerView.sMaxOffsetX) {
                        WindowManager wm = (WindowManager) mContext
                                .getSystemService(Context.WINDOW_SERVICE);

                        b = wm.getDefaultDisplay().getWidth();
                    }

                    startAnimator(a, b, ScreenLockerView.sDefaultDura);

                    return true;
                }
                return mGestreDetector.onTouchEvent(paramMotionEvent);
            }
        });
    }

    private void ShowDialog(final Context context) {
        Dialog dialog = new AlertDialog.Builder(context)
                .setIcon(R.drawable.icon).setTitle("退出").setMessage("是否退出")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ScreenLockerView.CloseFloatView();
                    }

                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }

                })

                .create();
        dialog.getWindow()
                .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private WindowManager.LayoutParams getLayoutParams() {
        WindowManager.LayoutParams param = new WindowManager.LayoutParams();

        param.width = 500;// WindowManager.LayoutParams.MATCH_PARENT;
        param.height = 500;// WindowManager.LayoutParams.MATCH_PARENT;
        param.format = PixelFormat.TRANSLUCENT;
        param.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        param.type = WindowManager.LayoutParams.TYPE_PHONE;
        param.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
        param.packageName = mContext.getPackageName();

        return param;
    }

    @Override
    public boolean onDown(MotionEvent paramMotionEvent) {
        return false;
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
        mFloatLayout.setX(paramMotionEvent2.getX() - paramMotionEvent1.getX());
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
        mFloatLayout.setX(AnimatorValue());
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (mFloatLayout.getX() != 0) {
            ScreenLockerView.CloseFloatView();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}

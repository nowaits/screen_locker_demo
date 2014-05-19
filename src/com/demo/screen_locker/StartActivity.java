
package com.demo.screen_locker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity {
    public static String sTag = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button b = (Button) findViewById(R.id.button);
        b.setText(ScreenLockerInitReceiver.isServiceWorked(this) ? "关闭屏保" : "打开屏保");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ScreenLockerInitReceiver.isServiceWorked(StartActivity.this)) {
                    ScreenLockerInitReceiver.StopService(StartActivity.this);
                    b.setText("打开屏保");
                } else {
                    ScreenLockerInitReceiver.StartService(StartActivity.this);
                    b.setText("关闭屏保");
                }

                
            }
        });
    }
}

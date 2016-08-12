package com.yn.test;


import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends Activity {

    private Thread thread;
    TextView textView;
    ImageView imageView;
    LinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.img);
        mLayout = (LinearLayout) findViewById(R.id.left);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Looper.prepare();
                        new android.os.Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                textView.setText("32");
                                textView.layout(textView.getLeft() + 10, textView.getTop(), textView.getRight(), textView.getBottom());
                            }
                        }.sendEmptyMessage(0);
                        Looper.loop();
                    }
                }).start();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

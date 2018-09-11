package com.kankanla.e560.m180907_morse;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Morse_key extends AppCompatActivity {
    private final String TAG = "###Morse_key###";
    private Button button1, button2;
    private TextView textView;
    private Morse_HZ morse_hz;
    private ListView listView;
    private Timer timer;
    private TimerTask timerTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_morse_key);
        button1 = findViewById(R.id.buttonkey1);
        button2 = findViewById(R.id.buttonkey2);
        textView = findViewById(R.id.cqcq);
        listView = findViewById(R.id.morselist);
        init();
        Intent intent = new Intent(this, Morse_HZ.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        button1.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "ACTION_DOWN");
                        morse_hz.VON();
                        textView.setBackgroundColor(Color.WHITE);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "ACTION_UP");
                        morse_hz.VOFF();
                        textView.setBackgroundColor(Color.BLACK);

                        break;
                    default:
                }

                return false;
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morse_hz != null) {
                    morse_hz.Test();
                    morse_hz.addCharacter("abcdefg");

                } else {
                    Log.d(TAG, "morse_hz == null");
                    return;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        Log.d(TAG, Settings.System.getString(this.getContentResolver(), "screen_brightness"));
    }

    private void init() {
        Log.d(TAG, "init");
        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//      // Keep screen off
//      getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 1.0f;
        getWindow().setAttributes(lp);
        Log.d(TAG, Settings.System.getString(this.getContentResolver(), "screen_brightness"));
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Morse_HZ.LocalBinder binder = (Morse_HZ.LocalBinder) service;
            morse_hz = binder.getService();
            morse_hz.loop_play();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            unbindService(this);
        }
    };

}

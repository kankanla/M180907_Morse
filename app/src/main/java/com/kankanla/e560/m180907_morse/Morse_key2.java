package com.kankanla.e560.m180907_morse;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;


public class Morse_key2 extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private final String TAG = "###Morse_key2###";
    private SharedPreferences shared;
    private Point point;
    private LinearLayout linearLayout;
    private RelativeLayout key_map_Layout;
    private ViewTreeObserver viewTreeObserver;
    private ArrayList<String> keyList;
    private Morse_HZ morse_hz;
    private String codetemp = null;
    private TextView textView;
    private Switch sw1, sw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        setContentView(R.layout.activity_morse_key2);
        Intent intent = new Intent(this, Morse_HZ.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        textView = findViewById(R.id.cqcq);

        setSharedXML();
        init();

        if (shared.getInt("key2_mapW", 0) == 0 || shared.getInt("key2_mapH", 0) == 0) {
            setKey_size();
        }

        sw1 = findViewById(R.id.sw_Ligth);
        sw2 = findViewById(R.id.sw_Sound);
        sw1.setChecked(shared.getBoolean("sw1", true));
        sw2.setChecked(shared.getBoolean("sw2", true));

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shared.edit().putBoolean("sw1", isChecked).commit();
                morse_hz.setLigthflag(isChecked);
                textView.setEnabled(isChecked);
            }
        });

        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shared.edit().putBoolean("sw2", isChecked).commit();
                morse_hz.setSoundflag(isChecked);
            }
        });
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Morse_HZ.LocalBinder binder = (Morse_HZ.LocalBinder) service;
            morse_hz = binder.getService();
            morse_hz.setTextView(textView);
            morse_hz.setHz(shared.getInt("HZ", Integer.parseInt(getString(R.string.set_sun2))));
            morse_hz.setBaseSpeed(shared.getInt("speed", Integer.parseInt(getString(R.string.set_speed2))));
            morse_hz.setSoundflag(shared.getBoolean("sw2", true));
            morse_hz.setLigthflag(shared.getBoolean("sw1", true));
            morse_hz.loop_play();
            morse_hz.setIntt(new Morse_HZ.getChang() {
                @Override
                public void MorseListChang(String x) {
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            unbindService(this);
        }
    };

    private void setKey_size() {
        Log.d(TAG, "setKey_size()");
        linearLayout = findViewById(R.id.key2_l1);
        key_map_Layout = findViewById(R.id.morse_key2);
        viewTreeObserver = linearLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onGlobalLayout() {
                Log.d(TAG, String.valueOf(linearLayout.getHeight()));
                shared.edit().putInt("key2_mapW", point.x - 5).apply();
                shared.edit().putInt("key2_mapH", point.y - linearLayout.getHeight() - 50).apply();
                createKey();
            }
        });
    }

    private void createKey() {
        keyList = new ArrayList<>();
        String temp = new String("QWERTYUIOPASDFGHJKL;ZXCVBNM,./0123456789");
        String temp2 = new String("QWERTASDFGZXCVBYUIOPHJKL;NM,./0123456789");
        char[] chars = temp2.toCharArray();
        for (char c : chars) {
            keyList.add(String.valueOf(c));
        }

        key_map_Layout = findViewById(R.id.morse_key2);
        TableLayout tableLayout = new TableLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tableLayout.setLayoutParams(params);
        tableLayout.setGravity(Gravity.CENTER_VERTICAL);
        for (int i = 0; i < 8; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < 5; j++) {
                int h = (int) shared.getInt("key2_mapH", 8) / 9;
                int w = (int) shared.getInt("key2_mapW", 5) / 5;
                Button button = new Button(this);
                button.setText(keyList.remove(0));
                button.setOnClickListener(this);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(w, h);
                button.setLayoutParams(layoutParams);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    button.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                } else {
                    button.setTextSize(30);
                }
                button.setPaddingRelative(0, 0, 0, 0);
                tableRow.addView(button);
            }
            tableRow.setGravity(Gravity.CENTER);
            tableLayout.addView(tableRow);
        }
        key_map_Layout.addView(tableLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        codetemp = (String) button.getText();
        if (morse_hz != null) {
            if (codetemp != null) {
                morse_hz.add_MorseString(codetemp);
            }
        } else {
            Log.d(TAG, "morse_hz == null");
            return;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    private void setSharedXML() {
        Log.d(TAG, "setSharedXML()");
        shared = getSharedPreferences("APP_SET", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        Display display = getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        editor.putInt("displayWidth", point.x);
        editor.putInt("displayHeight", point.y);
        editor.apply();
    }


    private void init() {
        Log.d(TAG, "init()");
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            morse_hz.Beep_ON();
            if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)) {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setBackgroundColor(Color.WHITE);
                    }
                });
            } else {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setBackground(getDrawable(R.drawable.out_window2));
                    }
                });
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            morse_hz.Beep_OFF();
            if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)) {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setBackgroundColor(Color.BLACK);
                    }
                });
            } else {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setBackground(getDrawable(R.drawable.out_window));
                    }
                });
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        morse_hz.ClearMorseList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        morse_hz.ClearMorseList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createKey();
        GoogleAdmob();
    }

    protected void GoogleAdmob() {

        AdView adView = new AdView(this);
        adView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_1));

        AdRequest.Builder adRequest = new AdRequest.Builder();
//        adRequest.addTestDevice(getString(R.string.addTestDeviceH));
//        adRequest.addTestDevice(getString(R.string.addTestDeviceASUS));
//        adRequest.addTestDevice(getString(R.string.addTestDeviceMI));
        adView.loadAd(adRequest.build());

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout layout = findViewById(R.id.morse_key2);
        layout.addView(adView, -1, layoutParams);
    }


}

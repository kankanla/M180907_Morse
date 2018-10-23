package com.kankanla.e560.m180907_morse;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class Morse_key2 extends AppCompatActivity {
    private final String TAG = "###Morse_key2###";
    private SharedPreferences shared;
    private Point point;
    private LinearLayout linearLayout;
    private RelativeLayout key_map_Layout;
    private ViewTreeObserver viewTreeObserver;
    private ArrayList<String> keyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_morse_key2);
        setSharedXML();
        init();

        if (shared.getInt("key2_mapW", 0) == 0 || shared.getInt("key2_mapH", 0) == 0) {
            setKey_size();
        }

        createKey();


    }

    private void setKey_size() {
        Log.d(TAG, "setKey_size()");
        linearLayout = findViewById(R.id.key2_l1);
        key_map_Layout = findViewById(R.id.morse_key2);
        viewTreeObserver = linearLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d(TAG, String.valueOf(linearLayout.getHeight()));
                shared.edit().putInt("key2_mapW", point.x - 5).apply();
                shared.edit().putInt("key2_mapH", point.y - linearLayout.getHeight() - 50);
            }
        });
    }

    private void createKey() {
        keyList = new ArrayList<>();
        for (int i = 0x41; i < 0x5B; i++) {
            keyList.add(String.valueOf((char) i));
        }
        for (int i = 0x30; i < 0x3A; i++) {
            keyList.add(String.valueOf((char) i));
        }

        Button button = new Button(this);
        button.setText(keyList.remove(0));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(shared.getInt("key2_mapW", 0), shared.getInt("key2_maph", 0));
        button.setLayoutParams(layoutParams);

        key_map_Layout.addView(button);
        Log.d(TAG, keyList.toString());


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

    @Override
    protected void onResume() {
        super.onResume();
        GoogleAdmob();
    }

    protected void GoogleAdmob() {
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        AdView adView = new AdView(this);
        adView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_1));

        AdRequest.Builder adRequest = new AdRequest.Builder();
        adRequest.addTestDevice(getString(R.string.addTestDeviceH));
        adRequest.addTestDevice(getString(R.string.addTestDeviceASUS));
        adView.loadAd(adRequest.build());

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout layout = findViewById(R.id.morse_key2);
        layout.addView(adView, -1, layoutParams);
    }


}

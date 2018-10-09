package com.kankanla.e560.m180907_morse;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "###MainActivity###";
    private Button buttonA, buttonB, buttonC;
    private ImageView imageView;
    private Intent intent;
    private Point point;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        GoogleAdmob();

        buttonA = findViewById(R.id.button1);
        buttonA.setText(getString(R.string.Morse_key));
        buttonA.setOnClickListener(this);

        buttonB = findViewById(R.id.button2);
        buttonB.setText(getString(R.string.Morse_Set));
        buttonB.setOnClickListener(this);

        buttonC = findViewById(R.id.button3);
        buttonC.setText(getString(R.string.Morse_Game));
        buttonC.setOnClickListener(this);
        buttonC.setEnabled(false);

        imageView = findViewById(R.id.imageView2);

        Display display = this.getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);

        Log.d(TAG, String.valueOf(point.x));
        Log.d(TAG, String.valueOf(point.y));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        intent = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick");
        switch (view.getId()) {
            case R.id.button1:
                intent = new Intent(this, Morse_key.class);
                startActivity(intent);
                break;
            case R.id.button2:
                intent = new Intent(this, Morse_Set.class);
                startActivity(intent);
                break;
            case R.id.button3:
                break;
            default:
                break;
        }
    }

    protected void GoogleAdmob() {
        AdView adView = new AdView(this);
//        adView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_1));

        AdRequest.Builder adRequest = new AdRequest.Builder();
        adRequest.addTestDevice("7026FA2EC1DC7E60FBEA02C64D33BD8B");
        adRequest.addTestDevice("53185CF5BFA5B2121DF7FA86E7064C22");
        adView.loadAd(adRequest.build());

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout layout = findViewById(R.id.main_admob);
        layout.addView(adView, -1, layoutParams);
    }
}

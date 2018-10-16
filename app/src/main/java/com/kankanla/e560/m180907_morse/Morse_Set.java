package com.kankanla.e560.m180907_morse;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Morse_Set extends AppCompatActivity implements View.OnClickListener {
    private Point point;
    private SharedPreferences shared;
    private Button fn3, fn4, fn5, fn6, fn7, fn8;
    private Button son1, son2, son3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse__set);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        son1 = findViewById(R.id.son1);
        son1.setText(getString(R.string.set_sun1) + "Hz");
        son1.setOnClickListener(this);
        son2 = findViewById(R.id.son2);
        son2.setText(getString(R.string.set_sun2) + "Hz");
        son2.setOnClickListener(this);
        son3 = findViewById(R.id.son3);
        son3.setText(getString(R.string.set_sun3) + "Hz");
        son3.setOnClickListener(this);

        fn3 = findViewById(R.id.Fn3);
        fn3.setOnClickListener(this);
        fn3.setText(getString(R.string.Fn3));
        fn4 = findViewById(R.id.Fn4);
        fn4.setOnClickListener(this);
        fn4.setText(getString(R.string.Fn4));
        fn5 = findViewById(R.id.Fn5);
        fn5.setOnClickListener(this);
        fn5.setText(getString(R.string.Fn5));
        fn6 = findViewById(R.id.Fn6);
        fn6.setOnClickListener(this);
        fn6.setText(getString(R.string.Fn6));
        fn7 = findViewById(R.id.Fn7);
        fn7.setOnClickListener(this);
        fn7.setText(getString(R.string.Fn7));
        fn8 = findViewById(R.id.Fn8);
        fn8.setOnClickListener(this);
        fn8.setText(getString(R.string.Fn8));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shared.getInt("admobe", 0) > 10) {
            GoogleAdmob();
        }
    }

    protected void init() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        point = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(point);

        shared = getSharedPreferences("APP_SET", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("displayWidth", point.x);
        editor.putInt("displayHeight", point.y);
        editor.apply();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        String btkey = (String) b.getText();
        Fun_Set fun_set = new Fun_Set(this);

        switch (v.getId()) {
            case R.id.Fn3:
                fun_set.setFnKey(btkey);
                fun_set.show();
                break;
            case R.id.Fn4:
                fun_set.setFnKey(btkey);
                fun_set.show();
                break;
            case R.id.Fn5:
                fun_set.setFnKey(btkey);
                fun_set.show();
                break;
            case R.id.Fn6:
                fun_set.setFnKey(btkey);
                fun_set.show();
                break;
            case R.id.Fn7:
                fun_set.setFnKey(btkey);
                fun_set.show();
                break;
            case R.id.Fn8:
                fun_set.setFnKey(btkey);
                fun_set.show();
                break;

            case R.id.son1:
                shared.edit().putInt("HZ", Integer.parseInt(getString(R.string.set_sun1))).apply();
                son1.setEnabled(false);
                son2.setEnabled(true);
                son3.setEnabled(true);
                break;
            case R.id.son2:
                shared.edit().putInt("HZ", Integer.parseInt(getString(R.string.set_sun2))).apply();
                son1.setEnabled(true);
                son2.setEnabled(false);
                son3.setEnabled(true);
                break;
            case R.id.son3:
                shared.edit().putInt("HZ", Integer.parseInt(getString(R.string.set_sun3))).apply();
                son1.setEnabled(true);
                son2.setEnabled(true);
                son3.setEnabled(false);
                break;
        }
    }

    class Fun_Set extends Dialog implements View.OnClickListener {
        private Context context;
        private String FnKey;
        private TextView t1, t2, t3;
        private Button b1, b2, b3;

        public Fun_Set(Context context) {
            super(context);
            this.context = context;
            this.FnKey = "noKey";
        }

        public Fun_Set(Context context, String FnKey) {
            super(context);
            this.context = context;
            this.FnKey = FnKey;
        }

        public void setFnKey(String fnKey) {
            FnKey = fnKey;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            View v = getLayoutInflater().from(context).inflate(R.layout.morse_set_dialog, null);

            LinearLayout linearLayout = v.findViewById(R.id.dialogLinear);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(point.x, LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayout.setLayoutParams(lp);

            t1 = v.findViewById(R.id.morse_set_dialogT1);
            t1.setText(FnKey);
            t2 = v.findViewById(R.id.morse_set_dialogT2);
            t2.setText(shared.getString(FnKey, "-.-. --.-"));
            t3 = v.findViewById(R.id.morse_set_dialogT3);
            t3.setText("");
            b1 = v.findViewById(R.id.morse_set_dialogB1);
            b1.setOnClickListener(this);
            b2 = v.findViewById(R.id.morse_set_dialogB2);
            b2.setOnClickListener(this);
            b3 = v.findViewById(R.id.morse_set_dialogB3);
            b3.setOnClickListener(this);
            setContentView(v);
        }

        @Override
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (t3.getText().toString().isEmpty()) {
                return;
            }
            SharedPreferences.Editor editor = shared.edit();
            editor.putString(FnKey, (String) t3.getText());
            editor.apply();
            editor.clear();
        }

        @Override
        public void onClick(View v) {
            StringBuilder temp = new StringBuilder();
            switch (v.getId()) {
                case R.id.morse_set_dialogB1:
                    temp.append(t3.getText());
                    temp.append(getString(R.string.set_key1));
                    t3.setText(temp.toString());
                    break;
                case R.id.morse_set_dialogB2:
                    temp.append(t3.getText());
                    temp.append(getString(R.string.set_key2));
                    t3.setText(temp.toString());
                    break;
                case R.id.morse_set_dialogB3:
                    temp.setLength(0);
                    t3.setText(temp);
                    break;
            }
        }
    }

    protected void GoogleAdmob() {
        AdView adView = new AdView(this);
//        adView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(getString(R.string.admob_1));

        AdRequest.Builder adRequest = new AdRequest.Builder();
        adRequest.addTestDevice(getString(R.string.addTestDeviceH));
        adRequest.addTestDevice(getString(R.string.addTestDeviceASUS));
        adView.loadAd(adRequest.build());

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout layout = findViewById(R.id.morseSet);
        layout.addView(adView, -1, layoutParams);
    }
}

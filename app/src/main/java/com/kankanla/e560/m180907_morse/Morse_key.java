package com.kankanla.e560.m180907_morse;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Morse_key extends AppCompatActivity {
    private final String TAG = "###Morse_key###";
    private Button button1, button2, button4;
    private TextView textView;
    private Morse_HZ morse_hz;
    private ListView listView;
    private Morse_SQL morse_sql;
    private Point point;
    private LinearLayout l1, l2, l3;
    private ViewTreeObserver viewTreeObserver;
    private SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_morse_key);
        getSupportActionBar().hide();
        init();
        shared = getSharedPreferences("APP_SET", MODE_PRIVATE);


        button1 = findViewById(R.id.buttonkey1);
        button2 = findViewById(R.id.buttonkey2);
        button4 = findViewById(R.id.button4);
        textView = findViewById(R.id.cqcq);
        listView = findViewById(R.id.morselist);

        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);

//        testonly
        viewTreeObserver = l3.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(layoutListener);


        if (shared.getInt("layout2_Height", 0) == 0 || shared.getInt("displayHeight", 0) != point.y) {
            viewTreeObserver = l3.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(layoutListener);
        } else {
            ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(point.x, shared.getInt("layout2_Height", 600) - 30);
            l2.setLayoutParams(layoutParams);
            GoogleAdmob();
        }

        Intent intent = new Intent(this, Morse_HZ.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        morse_sql = new Morse_SQL(this);
    }

    private ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(point.x, point.y - l1.getHeight() - l3.getHeight() - 30);
            l2.setLayoutParams(layoutParams);
            SharedPreferences.Editor editor = shared.edit();
            editor.putInt("displayWidth", point.x);
            editor.putInt("displayHeight", point.y);
            editor.putInt("layout1_Height", l1.getHeight());
            editor.putInt("layout2_Height", l2.getHeight());
            editor.putInt("layout3_Height", l3.getHeight());
            editor.commit();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            button1.setOnTouchListener(new View.OnTouchListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.d(TAG, "ACTION_DOWN");
                            morse_hz.Beep_ON();
                            textView.setBackgroundColor(Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.d(TAG, "ACTION_UP");
                            morse_hz.Beep_OFF();
                            textView.setBackgroundColor(Color.BLACK);
                            break;
                        default:
                    }
                    return false;
                }
            });
        } else {
            button1.setOnTouchListener(new View.OnTouchListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.d(TAG, "ACTION_DOWN");
                            morse_hz.Beep_ON();
                            textView.setBackground(getDrawable(R.drawable.out_window2));
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.d(TAG, "ACTION_UP");
                            morse_hz.Beep_OFF();
                            textView.setBackground(getDrawable(R.drawable.out_window));
                            break;
                        default:
                    }
                    return false;
                }
            });
        }

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morse_hz != null) {
                    morse_hz.Test();
                    morse_hz.add_MorseString("-.-. --.- -.-. --.- CQ CQ cq cq");
                } else {
                    Log.d(TAG, "morse_hz == null");
                    return;
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Add_Morse_item.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(morse_hz, view.getId() + " Item", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(morse_hz, view.getId() + " LongItem", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        Myadaputa myadaputa = new Myadaputa(morse_sql.list_item());
        listView.setAdapter(myadaputa);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        morse_hz.ClearMorseList();
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Display display = getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        //        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Morse_HZ.LocalBinder binder = (Morse_HZ.LocalBinder) service;
            morse_hz = binder.getService();
            morse_hz.setTextView(textView);
            morse_hz.loop_play();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            unbindService(this);
        }
    };


    class Myadaputa extends BaseAdapter {
        private Cursor cursor;
        private TextView list_t, list_c;

        public Myadaputa(Cursor cursor) {
            this.cursor = cursor;
        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            cursor.moveToPosition(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            cursor.moveToPosition(position);
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.morse_item_list, null);
                list_t = convertView.findViewById(R.id.list_title);
                list_c = convertView.findViewById(R.id.list_code);
            } else {
                list_t = convertView.findViewById(R.id.list_title);
                list_c = convertView.findViewById(R.id.list_code);
            }
            list_t.setText(cursor.getString(cursor.getColumnIndex("title")));
            list_c.setText(cursor.getString(cursor.getColumnIndex("acccond")));

            return convertView;
        }
    }

    protected void GoogleAdmob() {
        AdView adView = new AdView(this);
        adView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout layout = findViewById(R.id.rel_admob);
        layout.addView(adView, -1, layoutParams);
    }
}

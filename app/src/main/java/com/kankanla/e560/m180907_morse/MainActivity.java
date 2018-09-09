package com.kankanla.e560.m180907_morse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "###MainActivity###";
    private Button buttonA, buttonB, buttonC;
    private ImageView imageView;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        buttonA = findViewById(R.id.button1);
        buttonA.setText(getString(R.string.Morse_key));
        buttonA.setOnClickListener(this);

        buttonB = findViewById(R.id.button2);
        buttonB.setText(getString(R.string.Morse_Code));
        buttonB.setOnClickListener(this);

        buttonC = findViewById(R.id.button3);
        buttonC.setText(getString(R.string.Morse_Game));
        buttonC.setOnClickListener(this);

        imageView = findViewById(R.id.imageView2);

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
                break;
            case R.id.button3:
                break;
            default:
                break;
        }
    }
}

package com.kankanla.e560.m180907_morse;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Add_Morse_item extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "###Add_Morse_item###";
    private Morse_SQL morse_sql;
    private Button input_Fn1, input_Fn2, input_Fn3, input_Fn4, input_Fn5, input_Fn6, input_Fn7, input_Fn8;
    private Point point;
    private EditText editText;
    private TextInputEditText textInputEditText;
    private int item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "###onCreate###");
        setContentView(R.layout.activity_add_morse_item);
        getSupportActionBar().hide();
        init();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle == null) {
            item_id = 0;
        } else {
            item_id = bundle.getInt("id");
        }

        morse_sql = new Morse_SQL(this);
        textInputEditText = findViewById(R.id.TextInputEditText);
        editText = findViewById(R.id.editText_code);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        if (item_id == 0) {

        } else {
            String[] temp = morse_sql.get_item(item_id);
            for (String xxx : temp) {
                Log.d(TAG, "----------" + xxx + "-----------");
            }

            editText.setText(temp[1]);
            textInputEditText.setText(temp[0]);
        }

        input_Fn8 = findViewById(R.id.input_Fn8);
        input_Fn8.setText("AR");
        input_Fn8.setOnClickListener(this);

        input_Fn7 = findViewById(R.id.input_Fn7);
        input_Fn7.setText("RPT");
        input_Fn7.setOnClickListener(this);

        input_Fn6 = findViewById(R.id.input_Fn6);
        input_Fn6.setText("TU");
        input_Fn6.setOnClickListener(this);

        input_Fn2 = findViewById(R.id.input_Fn2);
        input_Fn2.setText(getString(R.string.add_item_Delete));
        if (item_id == 0) {
            input_Fn2.setEnabled(false);
        } else {
            input_Fn2.setEnabled(true);
            input_Fn2.setOnClickListener(this);
        }
        input_Fn1 = findViewById(R.id.input_Fn1);
        input_Fn1.setText(getString(R.string.add_item_Save));
        input_Fn1.setOnClickListener(this);


        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[0-9a-zA-Z@¥.¥_¥¥-]+$")) {
                    return source;
                } else {
                    return "";
                }
            }
        };
//        textInputEditText.setFilters(new InputFilter[]{inputFilter});
    }

    protected void init() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        point = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(point);
        GoogleAdmob();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TextInputEditText:
                break;
            case R.id.input_Fn8:
                if (textInputEditText.isFocused()) {
                    String inputCode = ".-.-."; //AR送信終了
                    Editable xx = textInputEditText.getText();
                    int temp = textInputEditText.getSelectionStart();
                    xx.insert(temp, inputCode);
                }
                break;
            case R.id.input_Fn7:
                if (textInputEditText.isFocused()) {
                    String inputCode = ".-.-.--.-"; //RPT送信終了
                    Editable xx = textInputEditText.getText();
                    int temp = textInputEditText.getSelectionStart();
                    xx.insert(temp, inputCode);
                }
                break;
            case R.id.input_Fn6:
                if (textInputEditText.isFocused()) {
                    String inputCode = "-..-"; //TU送信終了
                    Editable xx = textInputEditText.getText();
                    int temp = textInputEditText.getSelectionStart();
                    xx.insert(temp, inputCode);
                }
                break;
            case R.id.input_Fn2:
                // delete item
                if (item_id == 0) {
                    Toast.makeText(this, "item_id == 0", Toast.LENGTH_SHORT).show();
                    break;
                }
                editText.setText("");
                textInputEditText.setText("");
                morse_sql.del_temp(item_id);
                Toast.makeText(this, "del--" + String.valueOf(item_id), Toast.LENGTH_SHORT).show();
                this.finish();
                break;

            case R.id.input_Fn1:
                // save item
                if (String.valueOf(editText.getText()).isEmpty()) {
                    Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                    break;
                }
                morse_sql.add_item(editText.getText(), textInputEditText.getText(), item_id);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            default:
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

        RelativeLayout layout = findViewById(R.id.add_morse_item_admob);
        layout.addView(adView, -1, layoutParams);
    }
}

package com.kankanla.e560.m180907_morse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
import com.google.android.gms.ads.MobileAds;

public class Add_Morse_item extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "###Add_Morse_item###";
    private Morse_SQL morse_sql;
    private Button input_Fn1, input_Fn2, input_Fn3, input_Fn4, input_Fn5, input_Fn6, input_Fn7, input_Fn8;
    private Point point;
    private EditText editText;
    private TextInputEditText textInputEditText;
    private SharedPreferences shared;
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
        input_Fn8.setText(getString(R.string.Fn8));
        input_Fn8.setOnClickListener(this);

        input_Fn7 = findViewById(R.id.input_Fn7);
        input_Fn7.setText(getString(R.string.Fn7));
        input_Fn7.setOnClickListener(this);

        input_Fn6 = findViewById(R.id.input_Fn6);
        input_Fn6.setText(getString(R.string.Fn6));
        input_Fn6.setOnClickListener(this);

        input_Fn5 = findViewById(R.id.input_Fn5);
        input_Fn5.setText(getString(R.string.Fn5));
        input_Fn5.setOnClickListener(this);

        input_Fn4 = findViewById(R.id.input_Fn4);
        input_Fn4.setText(getString(R.string.Fn4));
        input_Fn4.setOnClickListener(this);

        input_Fn3 = findViewById(R.id.input_Fn3);
        input_Fn3.setText(getString(R.string.Fn3));
        input_Fn3.setOnClickListener(this);

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
        shared = getSharedPreferences("APP_SET", MODE_PRIVATE);
        point = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(point);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shared.getInt("admobe", 0) > 20) {
            GoogleAdmob();
        }
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String incode = null;
        Editable editable = null;
        int temp = 0;
        switch (v.getId()) {
            case R.id.TextInputEditText:
                break;
//            case R.id.input_Fn8:
//                if (textInputEditText.isFocused()) {
//                    String inputCode = ".-.-."; //AR送信終了
//                    Editable xx = textInputEditText.getText();
//                    int temp = textInputEditText.getSelectionStart();
//                    xx.insert(temp, inputCode);
//                }
//                break;
//            case R.id.input_Fn7:
//                if (textInputEditText.isFocused()) {
//                    String inputCode = ".-.-.--.-"; //RPT送信終了
//                    Editable xx = textInputEditText.getText();
//                    int temp = textInputEditText.getSelectionStart();
//                    xx.insert(temp, inputCode);
//                }
//                break;
//            case R.id.input_Fn6:
//                if (textInputEditText.isFocused()) {
//                    String inputCode = "-..-"; //TU送信終了
//                    Editable xx = textInputEditText.getText();
//                    int temp = textInputEditText.getSelectionStart();
//                    xx.insert(temp, inputCode);
//                }
//                break;

            case R.id.input_Fn1:
                // save item
                if (String.valueOf(editText.getText()).isEmpty()) {
                    Toast.makeText(this, getString(R.string.input_title_empt), Toast.LENGTH_SHORT).show();
                    break;
                }
                morse_sql.add_item(editText.getText(), textInputEditText.getText(), item_id);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                this.finish();
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
//                Toast.makeText(this, "del--" + String.valueOf(item_id), Toast.LENGTH_SHORT).show();
                this.finish();
                break;

            case R.id.input_Fn3:
//                了解 ••• ― •
//                訂正(HH) ••••••••
//                送信開始(BT) ― ••• ―
//                送信終了(AR) • ― • ― •
//                通信終了(VA) ••• ― • ―
//                送信要求(K) ― • ―
//                待機要求(AS) • ― •••

                //送信開始(BT) ― ••• ―
                incode = shared.getString((String) button.getText(), "-...-");
                editable = textInputEditText.getText();
                temp = textInputEditText.getSelectionStart();
                editable.insert(temp, incode);
                break;

            case R.id.input_Fn4:
                //送信終了(AR) • ― • ― •
                incode = shared.getString((String) button.getText(), ".-.-.");
                editable = textInputEditText.getText();
                temp = textInputEditText.getSelectionStart();
                editable.insert(temp, incode);
                break;

            case R.id.input_Fn5:
//                通信終了(VA) ••• ― • ―
                incode = shared.getString((String) button.getText(), "...-.-");
                editable = textInputEditText.getText();
                temp = textInputEditText.getSelectionStart();
                editable.insert(temp, incode);
                break;

            case R.id.input_Fn6:
//                送信要求(K) ― • ―
                incode = shared.getString((String) button.getText(), "-.-");
                editable = textInputEditText.getText();
                temp = textInputEditText.getSelectionStart();
                editable.insert(temp, incode);
                break;

            case R.id.input_Fn7:
//                待機要求(AS) • ― •••
                incode = shared.getString((String) button.getText(), ".-...");
                editable = textInputEditText.getText();
                temp = textInputEditText.getSelectionStart();
                editable.insert(temp, incode);
                break;

            case R.id.input_Fn8:
//                73 ― ― ••• ••• ― ―
                incode = shared.getString((String) button.getText(), "--... ...--");
                editable = textInputEditText.getText();
                temp = textInputEditText.getSelectionStart();
                editable.insert(temp, incode);
                break;

//            Button button = (Button) v;
//            String inputCode2 = (String) button.getText();
//            String inputCode = shared.getString(inputCode2, "--.-");
//            Editable xx = textInputEditText.getText();
//            int temp = textInputEditText.getSelectionStart();
//            xx.insert(temp, inputCode);

            default:

        }
    }

    protected void GoogleAdmob() {
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        AdView adView = new AdView(this);
//        adView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_1));

        AdRequest.Builder adRequest = new AdRequest.Builder();
        adRequest.addTestDevice(getString(R.string.addTestDeviceH));
        adRequest.addTestDevice(getString(R.string.addTestDeviceASUS));
        adView.loadAd(adRequest.build());

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout layout = findViewById(R.id.add_morse_item_admob);
        layout.addView(adView, -1, layoutParams);
    }
}

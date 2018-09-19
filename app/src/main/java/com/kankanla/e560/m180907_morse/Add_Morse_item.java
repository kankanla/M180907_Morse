package com.kankanla.e560.m180907_morse;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Add_Morse_item extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "###Add_Morse_item###";
    private Morse_SQL morse_sql;
    private Button button, button2, button3;
    private Button but_fn1;
    private Point point;
    private EditText editText;
    private TextInputEditText textInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_morse_item);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        point = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(point);
        morse_sql = new Morse_SQL(this);
        textInputEditText = findViewById(R.id.TextInputEditText);
        editText = findViewById(R.id.editText_code);

        button = findViewById(R.id.input_bt1);
        button.setText("AR");
        button.setOnClickListener(this);

        button2 = findViewById(R.id.input_bt2);
        button2.setText("RPT");
        button2.setOnClickListener(this);

        button2 = findViewById(R.id.input_bt3);
        button2.setText("TU");
        button2.setOnClickListener(this);

        but_fn1 = findViewById(R.id.item_save);
        but_fn1.setText("ADDITE");
        but_fn1.setOnClickListener(this);


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TextInputEditText:
                break;
            case R.id.input_bt1:
                if (textInputEditText.isFocused()) {
                    String inputCode = ".-.-."; //AR送信終了
                    Editable xx = textInputEditText.getText();
                    int temp = textInputEditText.getSelectionStart();
                    xx.insert(temp, inputCode);
                }
                break;
            case R.id.input_bt2:
                if (textInputEditText.isFocused()) {
                    String inputCode = ".-.-.--.-"; //RPT送信終了
                    Editable xx = textInputEditText.getText();
                    int temp = textInputEditText.getSelectionStart();
                    xx.insert(temp, inputCode);
                }
                break;
            case R.id.input_bt3:
                if (textInputEditText.isFocused()) {
                    String inputCode = "-..-"; //TU送信終了
                    Editable xx = textInputEditText.getText();
                    int temp = textInputEditText.getSelectionStart();
                    xx.insert(temp, inputCode);
                }
                break;
            case R.id.item_save:
                morse_sql.add_item(editText.getText(), textInputEditText.getText());
                editText.clearAnimation();
                textInputEditText.clearAnimation();
                break;
            default:
        }

    }
}

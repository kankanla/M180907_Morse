package com.kankanla.e560.m180907_morse;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Morse_Sound extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "###Morse_Sound###";
    private AudioTrack track;
    int flg = 0;
    private int qqa = 0, qqb = 0;
    private Button c, d, e, f, g, a, b;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse_sound);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        c = findViewById(R.id.cc);
        c.setOnClickListener(this);
        d = findViewById(R.id.dd);
        d.setOnClickListener(this);

        e = findViewById(R.id.ee);
        e.setOnClickListener(this);

        f = findViewById(R.id.ff);
        f.setOnClickListener(this);

        g = findViewById(R.id.gg);
        g.setOnClickListener(this);

        a = findViewById(R.id.aa);
        a.setOnClickListener(this);

        b = findViewById(R.id.bb);
        b.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        flg = 1;
        f1();
    }

    @Override
    protected void onPause() {
        flg = 0;
        super.onPause();
        track.stop();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void f1() {
        // getMinBufferSize(int sampleRateInHz, int channelConfig, int audioFormat) {

        int sampleRateInHz = 44100;
        final int buferSize = AudioTrack.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_8BIT);

        AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
        builder.setUsage(AudioAttributes.USAGE_MEDIA);

        AudioFormat.Builder builder1 = new AudioFormat.Builder();
        builder1.setSampleRate(sampleRateInHz);
        builder1.setEncoding(AudioFormat.ENCODING_PCM_8BIT);

        AudioTrack.Builder builder2 = new AudioTrack.Builder();
        builder2.setAudioAttributes(builder.build());
        builder2.setAudioFormat(builder1.build());
        builder2.setBufferSizeInBytes(buferSize);

        track = builder2.build();
        track.play();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int x = 44100;

                byte[] on = new byte[x];
                byte[] off = new byte[x];
                for (int i = 0; i < x; i++) {
                    if (i % 3 == 0) {
                        on[i] = 127;
                    } else {
                        on[i] = 80;
                    }
                    off[i] = 0;
                }

                while (true) {
                    track.write(on, 0, qqa);
                    track.write(off, 0, qqb);
                }
            }
        }).start();
    }

    private byte[] getaio() {
        int sampleRateInHz = 44100;
        int doo = 600;
        int bufsize = sampleRateInHz * 5;
        byte[] buf = new byte[bufsize];

        for (int i = 0; i < bufsize; i++) {
            double dd = (i / (sampleRateInHz / doo)) * (Math.PI * 2);
            double ddsin = Math.sin(dd);
            buf[i] = (byte) (ddsin > 0 ? Byte.MIN_VALUE : Byte.MIN_VALUE);
        }
        return buf;
    }

    private byte[] getSound() {
        int sampleRateInHz = 44100;
        int doo = 600;
        int buferSize = AudioTrack.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_8BIT);
        byte[] bufbyte = new byte[buferSize * 2];

        for (int i = 1500; i < buferSize; i++) {
            double dd = Math.sin((i / (sampleRateInHz / doo)) + Math.PI * 2);
            bufbyte[i] = (byte) (dd > 0.0 ? -128 : 00);
        }
        return bufbyte;
    }

    public byte[] createWaves(int sampleRate, int time) {
        Log.d(TAG, "createWaves");
        int dataNum = (int) ((double) sampleRate * ((double) time));
        byte[] data = new byte[dataNum];
        int flag = 0;
        for (int i = 0; i < dataNum; i = i + 2) {
            if (flag == 0) {
                data[i] = (byte) 0xff;
                data[i + 1] = (byte) 0xff;
                flag++;
            } else {
                data[i] = (byte) 0x00;
                data[i + 1] = (byte) 0x00;
                flag--;
            }
        }
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cc:
                qqa = 44100 / 261;
                qqb = 44100 / 261;
                break;
            case R.id.dd:
                qqa = 44100 / 293;
                qqb = 44100 / 293;
                break;
            case R.id.ee:
                qqa = 44100 / 329;
                qqb = 44100 / 329;
                break;
            case R.id.ff:
                qqa = 44100 / 349;
                qqb = 44100 / 349;
                break;
            case R.id.gg:
                qqa = 44100 / 391;
                qqb = 44100 / 391;
                break;
            case R.id.aa:
                qqa = 44100 / 220;
                qqb = 44100 / 220;
                break;
            case R.id.bb:
                qqa = 44100 / 246;
                qqb = 44100 / 246;
                break;
        }
    }
}
package com.kankanla.e560.m180907_morse;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Morse_HZ extends Service {
    private final String TAG = "###Morse_HZ###";
    private IBinder localBinder = new LocalBinder();
    private AudioTrack audioTrack;
    private int buferSize;
    private List<Character> morseList = new ArrayList<>();
    private int sampleRate = 4400;
    private int AsampleRate = 6000;
    private int flag = 0;
    private TextView textView;
    private int baseSpeed = 70;

    public Morse_HZ() {
        Log.d(TAG, "Morse_HZ");
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void add_MorseString(String string) {
        char[] character = string.toCharArray();
        for (char c : character) {
            addCharacter(getMorseCode(c));
            addCharacter(" ");
        }
    }


    public String getMorseCode(char c) {
        String temp = null;
        switch (c) {
            case ' ':
                temp = "  ";
                break;
            case 'a':
                temp = ".-";
                break;
            case 'A':
                temp = ".-";
                break;
            case 'b':
                temp = "-...";
                break;
            case 'B':
                temp = "-...";
                break;
            case 'c':
                temp = "-.-.";
                break;
            case 'C':
                temp = "-.-.";
                break;
            case 'd':
                temp = "--.";
                break;
            case 'D':
                temp = "--.";
                break;
            case 'e':
                temp = ".";
                break;
            case 'E':
                temp = ".";
                break;
            case 'f':
                temp = "--.-";
                break;
            case 'F':
                temp = "--.-";
                break;
            case 'g':
                temp = "--.";
                break;
            case 'G':
                temp = "--.";
                break;
            case 'h':
                temp = "....";
                break;
            case 'H':
                temp = "....";
                break;
            case 'i':
                temp = "..";
                break;
            case 'I':
                temp = "..";
                break;
            case 'j':
                temp = ".---";
                break;
            case 'J':
                temp = ".---";
                break;
            case 'k':
                temp = "-.-";
                break;
            case 'K':
                temp = "-.-";
                break;
            case 'l':
                temp = "--.-";
                break;
            case 'L':
                temp = "--.-";
                break;
            case 'm':
                temp = "--";
                break;
            case 'M':
                temp = "--";
                break;
            case 'n':
                temp = "-.";
                break;
            case 'N':
                temp = "-.";
            case 'o':
                temp = "---";
                break;
            case 'O':
                temp = "---";
                break;
            case 'p':
                temp = ".--.";
                break;
            case 'P':
                temp = ".--.";
                break;
            case 'q':
                temp = "--.-";
                break;
            case 'Q':
                temp = "--.-";
                break;
            case 'r':
                temp = ".-.";
                break;
            case 'R':
                temp = ".-.";
                break;
            case 's':
                temp = "...";
                break;
            case 'S':
                temp = "...";
                break;
            case 't':
                temp = "...";
                break;
            case 'T':
                temp = "...";
                break;
            default:
                temp = " ";
                break;
        }
        return temp;
    }


    public void addCharacter(String string) {
        Log.d(TAG, "addCharacter");
        char[] temp = string.toCharArray();
        for (Character c : temp) {
            morseList.add(c);
        }
        if (flag == 0) {
            CKC2();
        }
    }

    protected void CKC2() {
        flag = 1;
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                Log.d(TAG, "CKC2");
                while (true) {
                    try {
//                        System.out.println(morseList.get(0));
                        switch (morseList.remove(0)) {
                            case ' ':
                                VOFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.BLACK);
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                VOFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.BLACK);
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                break;
                            case '-':
                                VON();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.WHITE);
                                    }
                                });
                                Thread.sleep(baseSpeed * 3);
                                VOFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.BLACK);
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                break;
                            case '.':
                                VON();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.WHITE);
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                VOFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.BLACK);
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                break;
                        }
                    } catch (Exception e) {

                    }
                    if (morseList.isEmpty()) {
                        flag = 0;
                        break;
                    }
                }
            }
        }).start();
    }

    public void setsampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setAsampleRate(int AsampleRate) {
        this.sampleRate = AsampleRate;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void VOFF() {
        audioTrack.setVolume(0.0f);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void VON() {
        audioTrack.setVolume(1.0f);
    }

    public void loop_play() {
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                try {
                    buferSize = AudioTrack.getMinBufferSize(
                            sampleRate,
                            AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_8BIT
                    );

                    audioTrack = new AudioTrack(
                            AudioManager.STREAM_MUSIC,
                            sampleRate,
                            AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_8BIT,
                            buferSize,
                            AudioTrack.MODE_STREAM
                    );

                    audioTrack.reloadStaticData();

                    byte[] temp = createWaves(AsampleRate, 1);
                    for (; ; ) {
                        audioTrack.write(temp, 0, temp.length);
                        if (audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
                            audioTrack.play();
                            audioTrack.setVolume(0.0f);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (audioTrack != null) {
                        audioTrack.stop();
                        audioTrack.release();
                    }
                }
            }
        });
        thread.start();
    }

    public byte[] createWaves(int sampleRate, int time) {
        int dataNum = (int) ((double) sampleRate * ((double) time / 1000.0));
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
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        Log.d(TAG, "onBind");
        return localBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public class LocalBinder extends Binder implements IBinder {
        public Morse_HZ getService() {
            // Return this instance of LocalService so clients can call public methods
            Log.d(TAG, "LocalBinder");
            return Morse_HZ.this;
        }
    }

    public void Test() {
        Log.d(TAG, "BinderServersuccess");
        Toast.makeText(this, "BinderServersuccess", Toast.LENGTH_SHORT).show();
    }
}

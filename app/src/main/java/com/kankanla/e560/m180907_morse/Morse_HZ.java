package com.kankanla.e560.m180907_morse;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Morse_HZ extends Service {
    private final String TAG = "###Morse_HZ###";
    private IBinder localBinder = new LocalBinder();
    private AudioTrack audioTrack;
    private int buferSize;

    private List<Character> morseList = new ArrayList<>();
    private int Beeppling = 0;
    private int sampleRate = 44100;
    private int hz;
    private int flag = 0;
    private boolean soundflag = true;
    private boolean ligthflag = true;
    private TextView textView;
    private int baseSpeed = 90;
    private int beep_size = 0;
    private byte[] beep_on, beep_off;
    private Semaphore semaphore = new Semaphore(1);
    private HashMap<String, String> CODE = new HashMap<>();
    private SharedPreferences shared;
    private getChang getChang;

    interface getChang {
        void MorseListChang(String x);
    }

    public void setIntt(getChang getChang) {
        this.getChang = getChang;

    }

    public Morse_HZ() {
        Log.d(TAG, "Morse_HZ");
    }

    public Morse_HZ(getChang getChang) {
        Log.d(TAG, "Morse_HZ");
        this.getChang = getChang;
    }

    {
        CODE.put("-", "-");
        CODE.put("‐", "-");
        CODE.put(".", "・");
        CODE.put("・", ".");
        CODE.put(" ", " ");

        CODE.put("A", ".-");
        CODE.put("B", "-...");
        CODE.put("C", "-.-.");
        CODE.put("D", "-..");
        CODE.put("E", ".");
        CODE.put("F", "..-.");
        CODE.put("G", "--.");
        CODE.put("H", "....");
        CODE.put("I", "..");
        CODE.put("J", ".---");
        CODE.put("K", "-.-");
        CODE.put("L", ".-..");
        CODE.put("M", "--");
        CODE.put("N", "-.");
        CODE.put("O", "---");
        CODE.put("P", ".--.");
        CODE.put("Q", "--.-");
        CODE.put("R", ".-.");
        CODE.put("S", "...");
        CODE.put("T", "-");
        CODE.put("U", "..-");
        CODE.put("V", "...-");
        CODE.put("W", ".--");
        CODE.put("X", "-..-");
        CODE.put("Y", "-.--");
        CODE.put("Z", "--..");
        CODE.put("a", ".-");
        CODE.put("b", "-...");
        CODE.put("c", "-.-.");
        CODE.put("d", "-..");
        CODE.put("e", ".");
        CODE.put("f", "..-.");
        CODE.put("g", "--.");
        CODE.put("h", "....");
        CODE.put("i", "..");
        CODE.put("j", ".---");
        CODE.put("k", "-.-");
        CODE.put("l", ".-..");
        CODE.put("m", "--");
        CODE.put("n", "-.");
        CODE.put("o", "---");
        CODE.put("p", ".--.");
        CODE.put("q", "--.-");
        CODE.put("r", ".-.");
        CODE.put("s", "...");
        CODE.put("t", "-");
        CODE.put("u", "..-");
        CODE.put("v", "...-");
        CODE.put("w", ".--");
        CODE.put("x", "-..-");
        CODE.put("y", "-.--");
        CODE.put("z", "--..");

        CODE.put("1", ".----");
        CODE.put("2", "..---");
        CODE.put("3", "...--");
        CODE.put("4", "....-");
        CODE.put("5", ".....");
        CODE.put("6", "-....");
        CODE.put("7", "--...");
        CODE.put("8", "---..");
        CODE.put("9", "----.");
        CODE.put("0", "-----");

        CODE.put(",", "--..--");
        CODE.put("?", "..--..");
        CODE.put("!", "-.-.--");
        CODE.put("/", "-..-.");
        CODE.put("@", ".--.-.");
        CODE.put("(", "-.--.");
        CODE.put(")", "-.--.-");
    }

    public void setTextView(TextView textView) {
        Log.d(TAG, "setTextView");
        this.textView = textView;
    }

    public void setSoundflag(boolean soundflag) {
        this.soundflag = soundflag;
    }

    public void setLigthflag(boolean ligthflag) {
        this.ligthflag = ligthflag;
    }

    public void add_MorseString(String string) {
        Log.d(TAG, "add_MorseString");
        String temp = null;
        char[] character = string.toCharArray();
        for (char c : character) {
            temp = CODE.get(String.valueOf(c));
            if (temp != null) {
                addCharacter(CODE.get(String.valueOf(c)));
                if (c != '・' && c != ' ' && c != '-' && c != '.') {
                    addCharacter(" ");
                }
            } else {
                addCharacter(" ");
            }
        }
    }

    public List<Character> getMorseList() {
        return morseList;
    }

    public void ClearMorseList() {
        morseList.clear();
    }

    public void addCharacter(String string) {
        Log.d(TAG, "addCharacter");
        char[] temp = string.toCharArray();
        getChang.MorseListChang(string);
        for (Character c : temp) {
            morseList.add(c);
        }
        if (flag == 0) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CKC3();
            } else {
                CKC2();
            }
        }
    }

    protected void CKC3() {
        Log.d(TAG, "CKC3");
        flag = 1;
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                while (true) {
                    try {
                        semaphore.acquire();
                        switch (morseList.remove(0)) {
                            case ' ':
                                Beep_OFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.BLACK);
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                Beep_OFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.BLACK);
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                break;
                            case '-':
                                Beep_ON();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.WHITE);
                                    }
                                });
                                Thread.sleep(baseSpeed * 3);
                                Beep_OFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.BLACK);
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                break;
                            case '・':
                                Beep_ON();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.WHITE);
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                Beep_OFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.BLACK);
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                break;
                            case '.':
                                Beep_ON();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.WHITE);
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                Beep_OFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackgroundColor(Color.BLACK);
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                break;
                            default:
                                break;
                        }
                        semaphore.release();
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


    protected void CKC2() {
        Log.d(TAG, "CKC2");
        flag = 1;
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                while (true) {
                    try {
                        semaphore.acquire();
                        switch (morseList.remove(0)) {
                            case ' ':
                                Beep_OFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackground(getDrawable(R.drawable.out_window));
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                Beep_OFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackground(getDrawable(R.drawable.out_window));
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                break;
                            case '-':
                                Beep_ON();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackground(getDrawable(R.drawable.out_window2));
                                    }
                                });
                                Thread.sleep(baseSpeed * 3);
                                Beep_OFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackground(getDrawable(R.drawable.out_window));
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                break;
                            case '・':
                                Beep_ON();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackground(getDrawable(R.drawable.out_window2));
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                Beep_OFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackground(getDrawable(R.drawable.out_window));
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                break;
                            case '.':
                                Beep_ON();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackground(getDrawable(R.drawable.out_window2));
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                Beep_OFF();
                                textView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setBackground(getDrawable(R.drawable.out_window));
                                    }
                                });
                                Thread.sleep(baseSpeed * 1);
                                break;
                            default:
                                break;
                        }
                        semaphore.release();
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

    public void setHz(int hz) {
        this.hz = hz;
    }

    public void setBaseSpeed(int baseSpeed) {
        this.baseSpeed = baseSpeed;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Beep_OFF() {
        audioTrack.setVolume(0.0f);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Beep_ON() {
        audioTrack.setVolume(1.0f);
    }

    public void loop_play() {
        if (Beeppling == 1) {
            return;
        }
        Log.d(TAG, "loop_play");
        Beeppling = 1;
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                try {
                    buferSize = AudioTrack.getMinBufferSize(
                            sampleRate,
                            AudioFormat.CHANNEL_OUT_STEREO,
                            AudioFormat.ENCODING_PCM_16BIT
                    );

                    audioTrack = new AudioTrack(
                            AudioManager.STREAM_MUSIC,
                            sampleRate,
                            AudioFormat.CHANNEL_OUT_STEREO,
                            AudioFormat.ENCODING_PCM_16BIT,
                            buferSize,
                            AudioTrack.MODE_STREAM
                    );

                    byte[] bs = new byte[44100];
                    Double temp;
                    for (int i = 0; i < bs.length; i++) {
                        temp = i / 2 * Math.PI / (44100 / (double) hz);
                        bs[i] = (byte) (Math.sin(temp) * 120);
                    }

                    while (true) {
                        if (soundflag) {
                            audioTrack.write(bs, 0, bs.length);
                            if (audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
                                audioTrack.play();
                                audioTrack.setVolume(0.0f);
                            }
                        } else {
                            audioTrack.setVolume(0.0f);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (audioTrack != null) {
                        audioTrack.stop();
                        audioTrack.release();
                        Beeppling = 0;
                    }
                }
            }
        });
        thread.start();
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

    public class LocalBinder2 extends Binder implements IBinder {
        public Morse_HZ getService(getChang getChang) {
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

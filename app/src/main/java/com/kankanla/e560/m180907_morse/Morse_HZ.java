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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

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
    private Semaphore semaphore = new Semaphore(1);
    private HashMap<String, String> CODE = new HashMap<>();

    public Morse_HZ() {
        Log.d(TAG, "Morse_HZ");
    }

    {
        CODE.put(" ", " ");
        CODE.put("A", ".-");
        CODE.put("B", "-..");
        CODE.put("C", "-.-.");
        CODE.put("D", "--.");
        CODE.put("E", ".");
        CODE.put("F", "..-.");
        CODE.put("G", "--.");
        CODE.put("H", "....");
        CODE.put("I", "..");
        CODE.put("J", ".---");
        CODE.put("K", ".-.");
        CODE.put("L", "--.-");
        CODE.put("M", "--");
        CODE.put("N", "-.");
        CODE.put("O", "---");
        CODE.put("P", ".--.");
        CODE.put("Q", "--.-");
        CODE.put("R", "-.-");
        CODE.put("S", "---");
        CODE.put("T", "-");
        CODE.put("U", "..-");
        CODE.put("V", "...-");
        CODE.put("W", ".--");
        CODE.put("X", "-..-");
        CODE.put("Y", "-.--");
        CODE.put("Z", "--..");
        CODE.put("a", ".-");
        CODE.put("b", "-..");
        CODE.put("c", "-.-.");
        CODE.put("d", "--.");
        CODE.put("e", ".");
        CODE.put("f", "..-.");
        CODE.put("g", "--.");
        CODE.put("h", "....");
        CODE.put("i", "..");
        CODE.put("j", ".---");
        CODE.put("k", ".-.");
        CODE.put("l", "--.-");
        CODE.put("m", "--");
        CODE.put("n", "-.");
        CODE.put("0", "---");
        CODE.put("p", ".--.");
        CODE.put("q", "--.-");
        CODE.put("r", "-.-");
        CODE.put("s", "---");
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

        CODE.put(".", ".-.-.-");
        CODE.put(",", "--..--");
        CODE.put("?", "..--..");
        CODE.put("!", "-.-.--");
        CODE.put("-", "-....-");
        CODE.put("/", "-..-.");
        CODE.put("@", ".--.-.");
        CODE.put("(", "-.--.");
        CODE.put(")", "-.--.-");
        CODE.put(")", "-.--.-");
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void add_MorseString(String string) {
        String temp = null;
        char[] character = string.toCharArray();
        for (char c : character) {
            temp = CODE.get(String.valueOf(c));
            if (temp != null) {
                addCharacter(CODE.get(String.valueOf(c)));
                addCharacter(" ");
            } else {
                addCharacter(" ");
            }
        }
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
                        Log.e(TAG, "CKC2");
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
    public void Beep_OFF() {
        audioTrack.setVolume(0.0f);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Beep_ON() {
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

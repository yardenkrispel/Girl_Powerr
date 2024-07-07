package com.example.Girl_Power.Utilities;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SoundUtil {

    private Context context;
    private Executor executor;
    private Handler handler;
    private MediaPlayer mediaPlayer;

    public SoundUtil(Context context)
    {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void playSound(int soundId){
        executor.execute(() -> {
            mediaPlayer = MediaPlayer.create(context, soundId);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(1.0f,1.0f);
            mediaPlayer.start();
        });
    }

    public void stopSound() {
        if (mediaPlayer != null) {
            executor.execute(() -> {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            });
        }
    }
}

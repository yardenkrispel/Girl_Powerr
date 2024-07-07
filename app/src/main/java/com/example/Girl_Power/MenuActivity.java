package com.example.Girl_Power;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.Girl_Power.Utilities.ImageLoader;
import com.example.Girl_Power.Utilities.SoundUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MenuActivity extends AppCompatActivity {

    ImageLoader imgLoader = new ImageLoader(this);
    private SoundUtil soundUtil = new SoundUtil(this);
    private boolean backgroundSoundOn = false;
    private Timer backgroundSoundTimer;
    private ShapeableImageView menu_IMG_background;
    private MaterialButton menu_BTN_buttons_mode;
    private MaterialButton menu_BTN_tilt_mode;
    private MaterialButton menu_BTN_scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        initViews();
        getPermission();
    }

    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    private void findViews() {
        menu_IMG_background = findViewById(R.id.menu_IMG_background);
        menu_BTN_buttons_mode = findViewById(R.id.menu_BTN_buttons_mode);
        menu_BTN_tilt_mode = findViewById(R.id.menu_BTN_tilt_mode);
        menu_BTN_scores = findViewById(R.id.menu_BTN_scores);
    }

    private void initViews() {
        menu_BTN_buttons_mode.setOnClickListener(v -> redirectToGame(GameActivity.BUTTONS_GAME_MODE));
        menu_BTN_tilt_mode.setOnClickListener(v -> redirectToGame(GameActivity.SENSOR_GAME_MODE));
        menu_BTN_scores.setOnClickListener(v -> redirectToTopRecords());
    }

    private void redirectToGame(String mode) {
        Intent gameIntent = new Intent(this, GameActivity.class);
        gameIntent.putExtra(GameActivity.KEY_GAME_MODE, mode);
        startActivity(gameIntent);
        finish();
    }

    private void redirectToTopRecords() {
        Intent topRecordsIntent = new Intent(this, TopRecordsActivity.class);
        startActivity(topRecordsIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startBackgroundSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundSound();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopBackgroundSound();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopBackgroundSound();
    }

    private void startBackgroundSound() {
        if (!backgroundSoundOn) {
            backgroundSoundTimer = new Timer();

            backgroundSoundTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    soundUtil.playSound(R.raw.menu);
                }
            }, 0, 140000);
            backgroundSoundOn = true;
        }
    }

    private void stopBackgroundSound() {
        if (backgroundSoundOn) {
            backgroundSoundTimer.cancel();
            soundUtil.stopSound();
            backgroundSoundOn = false;
        }
    }

}
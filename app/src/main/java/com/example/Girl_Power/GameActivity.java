package com.example.Girl_Power;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.Girl_Power.Interfaces.MoveCallBack;
import com.example.Girl_Power.Logic.GameManager;
import com.example.Girl_Power.Model.EntityType;
import com.example.Girl_Power.Utilities.ImageLoader;
import com.example.Girl_Power.Utilities.RecordsUtil;
import com.example.Girl_Power.Utilities.SensorsUtil;
import com.example.Girl_Power.Utilities.SoundUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private long DELAY = 1000;
    private final int ROWS = 7;
    private final int COLS = 5;
    private final int MAX_LIVES = 3;
    private final int INTERVAL = 3;     // interval between obstacles creation
    public static final String KEY_GAME_MODE = "KEY_GAME_MODE";
    public static final String SENSOR_GAME_MODE = "SENSOR";
    public static final String BUTTONS_GAME_MODE = "BUTTONS";
    public String gameMode;
    private ImageLoader imgLoader = new ImageLoader(this);
    private SensorsUtil sensorsUtil;
    private SoundUtil soundUtil = new SoundUtil(this);
    private boolean timerOn = false;
    private boolean backgroundSoundOn = false;
    private boolean isSpeedUp = false;
    private ShapeableImageView[] lives;
    private ShapeableImageView[][] cells;   // game cells consist of player area (first line) and obstacles area
    private MaterialButton main_BTN_right;
    private MaterialButton main_BTN_left;
    private MaterialTextView main_LBL_score;
    private GameManager gameManager;    // handles all the games logic
    private Timer timer;
    private Timer backgroundSoundTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameManager = new GameManager(MAX_LIVES, ROWS, COLS, INTERVAL);

        findViews();
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (gameMode.equals(SENSOR_GAME_MODE)) {
            sensorsUtil.start();
        }
        startGame();
        startBackgroundSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameMode.equals(SENSOR_GAME_MODE)) {
            sensorsUtil.start();
        }
        startGame();
        startBackgroundSound();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (gameMode.equals(SENSOR_GAME_MODE)) {
            sensorsUtil.stop();
        }
        stopGame();
        stopBackgroundSound();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameMode.equals(SENSOR_GAME_MODE)) {
            sensorsUtil.stop();
        }
        stopGame();
        stopBackgroundSound();
    }

    private void findViews() {
        main_LBL_score = findViewById(R.id.main_LBL_score);
        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);

        lives = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };

        cells = new ShapeableImageView[ROWS][COLS];

        cells = new ShapeableImageView[][]{
                {
                        findViewById(R.id.main_IMG_cell00),
                        findViewById(R.id.main_IMG_cell01),
                        findViewById(R.id.main_IMG_cell02),
                        findViewById(R.id.main_IMG_cell03),
                        findViewById(R.id.main_IMG_cell04),
                },
                {
                        findViewById(R.id.main_IMG_cell10),
                        findViewById(R.id.main_IMG_cell11),
                        findViewById(R.id.main_IMG_cell12),
                        findViewById(R.id.main_IMG_cell13),
                        findViewById(R.id.main_IMG_cell14),
                },
                {
                        findViewById(R.id.main_IMG_cell20),
                        findViewById(R.id.main_IMG_cell21),
                        findViewById(R.id.main_IMG_cell22),
                        findViewById(R.id.main_IMG_cell23),
                        findViewById(R.id.main_IMG_cell24),
                },
                {
                        findViewById(R.id.main_IMG_cell30),
                        findViewById(R.id.main_IMG_cell31),
                        findViewById(R.id.main_IMG_cell32),
                        findViewById(R.id.main_IMG_cell33),
                        findViewById(R.id.main_IMG_cell34),
                },
                {
                        findViewById(R.id.main_IMG_cell40),
                        findViewById(R.id.main_IMG_cell41),
                        findViewById(R.id.main_IMG_cell42),
                        findViewById(R.id.main_IMG_cell43),
                        findViewById(R.id.main_IMG_cell44),
                },
                {
                        findViewById(R.id.main_IMG_cell50),
                        findViewById(R.id.main_IMG_cell51),
                        findViewById(R.id.main_IMG_cell52),
                        findViewById(R.id.main_IMG_cell53),
                        findViewById(R.id.main_IMG_cell54),
                },
                {
                        findViewById(R.id.main_IMG_cell60),
                        findViewById(R.id.main_IMG_cell61),
                        findViewById(R.id.main_IMG_cell62),
                        findViewById(R.id.main_IMG_cell63),
                        findViewById(R.id.main_IMG_cell64),
                },

        };
    }

    private void initViews() {
        Intent previousScreen = getIntent();

        if (previousScreen.getStringExtra(KEY_GAME_MODE).equals(SENSOR_GAME_MODE)) {
            initSensorsUtil();
            main_BTN_left.setVisibility(View.INVISIBLE);
            main_BTN_right.setVisibility(View.INVISIBLE);
            gameMode = SENSOR_GAME_MODE;
        } else {
            main_BTN_left.setOnClickListener(v -> {
                moveLeft();
            });
            main_BTN_right.setOnClickListener(v -> {
                moveRight();
            });
            gameMode = BUTTONS_GAME_MODE;
        }

        for (int i = 0; i < MAX_LIVES; i++) {
            imgLoader.loadImg(R.drawable.girl_life, lives[i]);
        }

        for (int i = 0; i < COLS; i++) {
            imgLoader.loadImg(R.drawable.girl, cells[ROWS - 1][i]);
        }
    }

    private void startGame() {
        if (!timerOn) {
            timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> nextCycle());
                }
            }, 1000, DELAY);
            timerOn = true;
        }
    }

    private void stopGame() {
        if (timerOn) {
            timer.cancel();
            timerOn = false;
        }
    }

    private void startBackgroundSound() {
        if (!backgroundSoundOn) {
            backgroundSoundTimer = new Timer();

            backgroundSoundTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    soundUtil.playSound(R.raw.game);
                }
            }, 0, 100000);
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

    private void endGame() {
        toast("GAME OVER");
        stopGame();
        saveRecord();
        redirectToMenu();
    }

    private void saveRecord() {
        double lat = 0;
        double lon = 0;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // request permission if required
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
        }

        RecordsUtil.getInstance().addRecord(gameManager.getScore(), lat, lon);
    }

    private void initSensorsUtil() {
        sensorsUtil = new SensorsUtil(this, new MoveCallBack() {
            @Override
            public void tiltRight() {
                moveRight();
            }

            @Override
            public void tiltLeft() {
                moveLeft();
            }

            @Override
            public void tiltUp() {
                speedUp();
            }

            @Override
            public void tiltDown() {
                slowDown();
            }
        });
        sensorsUtil.start();
    }

    private void moveLeft() {
        gameManager.movePlayerLeft();
        refreshPlayerArea();
    }

    private void moveRight() {
        gameManager.movePlayerRight();
        refreshPlayerArea();
    }

    private void speedUp() {
        if (!isSpeedUp) {
            stopGame();
            DELAY = 300;
            startGame();
            isSpeedUp = true;
        }
    }

    private void slowDown() {
        if (isSpeedUp) {
            stopGame();
            DELAY = 1000;
            startGame();
            isSpeedUp = false;
        }
    }

    // update the game logic after each clock cycle
    private void nextCycle() {
        int isHit = gameManager.moveEntities(); // 0-not hit, 1-hit obstacle,  2-hit reward, 3-hit life

        if (isHit == 1) {
            vibrate();
            soundUtil.playSound(R.raw.bowser_hit_sound);
        }

        if (isHit == 2) {
            soundUtil.playSound(R.raw.coin);
        }

        if (isHit == 3) {
            soundUtil.playSound(R.raw.life);
        }

        if (gameManager.isLost()) {
            soundUtil.playSound(R.raw.game_over);
            endGame();
            return;
        }

        //separate refresh actions - motivation is the times that only player moves
        refreshPlayerArea();
        refreshEntitiesArea();
        refreshUpperSection();
    }

    // refresh player area (first line)
    private void refreshPlayerArea() {
        int[] playerCords = gameManager.getEntitiesCords()[0];  // player coordinates always the first in array

        for (int i = 0; i < COLS; i++) {
            cells[ROWS - 1][i].setVisibility(View.INVISIBLE);   // make player area invisible
        }
        cells[playerCords[0]][playerCords[1]].setVisibility(View.VISIBLE); // make player current cell visible
    }

    // refresh obstacles area
    private void refreshEntitiesArea() {
        int[][] layoutState = gameManager.getEntitiesCords();   // coordinates of all existing obstacles
        EntityType[] entitiesType = gameManager.getEntitiesType();

        for (int i = 1; i < layoutState.length; i++) {
            int row = layoutState[i][0];
            int col = layoutState[i][1];
            EntityType entityType = entitiesType[i];

            //make obstacle previous location invisible
            if (row > 0) {
                cells[row - 1][col].setVisibility(View.INVISIBLE);
            }

            //won't display obstacles in player area (first cell)
            if (row < ROWS - 1) {
                cells[row][col].setVisibility(View.VISIBLE);

                if (entityType == EntityType.OBSTACLE) {
                    imgLoader.loadImg(R.drawable.ghost, cells[row][col]);
                } else if (entityType == EntityType.REWARD) {
                    imgLoader.loadImg(R.drawable.coin, cells[row][col]);
                } else {
                    imgLoader.loadImg(R.drawable.capsule, cells[row][col]);
                }
            }
        }
    }

    public void refreshUpperSection() {
        String text = "Score: " + gameManager.getScore();
        main_LBL_score.setText(text);

        if (gameManager.getHits() == 0) {
            lives[0].setVisibility(View.VISIBLE);
            lives[1].setVisibility(View.VISIBLE);
        } else if (gameManager.getHits() == 1) {
            lives[0].setVisibility(View.INVISIBLE);
            lives[1].setVisibility(View.VISIBLE);
        } else if (gameManager.getHits() == 2) {
            lives[0].setVisibility(View.INVISIBLE);
            lives[1].setVisibility(View.INVISIBLE);
        }
    }

    private void redirectToMenu() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
        finish();
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }
}
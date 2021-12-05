package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    // Sensors and vibrator
    private static Vibrator vibrator;
    private static Sensor accSensor;
    private static SensorManager sensorManager;

    // Panel objects
    private ImageButton panel_BTN_left;
    private ImageButton panel_BTN_right;
    private ImageView[][] panel_IMG_matrix;
    private ImageView[] panel_ICN_hearts;
    private TextView panel_TXT_score;

    // Bundle objects
    private static Bundle bundle;
    private String nickname = "";
    private boolean vibratorFlag, sensorsFlag;
    private double lat = 0, lng = 0;

    // Constants
    public static final String LAT = "LAT";
    public static final String LNG = "LNG";
    public static final String NICKNAME = "NICKNAME";
    public static final String VIBRATOR_FLAG = "VIBRATOR_FLAG";
    public static final String SENSORS_FLAG = "SENSORS_FLAG";

    // Game Controller
    private GameController gameController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViews();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initSensor();

        unpackBundle();

        GameView gameView = new GameView(
                this,
                panel_BTN_left,
                panel_BTN_right,
                panel_IMG_matrix,
                panel_ICN_hearts,
                panel_TXT_score,
                vibratorFlag);
        GameModel gameModel = new GameModel(
                nickname,
                lat,
                lng);
        gameController = new GameController(gameView, gameModel, sensorsFlag);
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameController.initGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameController.startGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameController.stopGame();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public static Bundle getBundle() {
        return bundle;
    }

    private void unpackBundle() {
        bundle = getIntent().getBundleExtra(MainActivity.BUNDLE);
        nickname = bundle.getString(NICKNAME);
        sensorsFlag = bundle.getBoolean(SENSORS_FLAG);
        vibratorFlag = bundle.getBoolean(VIBRATOR_FLAG);
        lat = bundle.getDouble(LAT);
        lng = bundle.getDouble(LNG);
    }

    private void findViews() {

        panel_BTN_left = findViewById(R.id.panel_BTN_left);
        panel_BTN_right = findViewById(R.id.panel_BTN_right);

        panel_TXT_score = findViewById(R.id.panel_TXT_score);

        panel_IMG_matrix = new ImageView[][] {
                new ImageView[] {
                        findViewById(R.id.panel_IMG_cell00),
                        findViewById(R.id.panel_IMG_cell01),
                        findViewById(R.id.panel_IMG_cell02),
                        findViewById(R.id.panel_IMG_cell03),
                        findViewById(R.id.panel_IMG_cell04)
                },
                new ImageView[] {
                        findViewById(R.id.panel_IMG_cell10),
                        findViewById(R.id.panel_IMG_cell11),
                        findViewById(R.id.panel_IMG_cell12),
                        findViewById(R.id.panel_IMG_cell13),
                        findViewById(R.id.panel_IMG_cell14)
                },
                new ImageView[] {
                        findViewById(R.id.panel_IMG_cell20),
                        findViewById(R.id.panel_IMG_cell21),
                        findViewById(R.id.panel_IMG_cell22),
                        findViewById(R.id.panel_IMG_cell23),
                        findViewById(R.id.panel_IMG_cell24)
                },
                new ImageView[] {
                        findViewById(R.id.panel_IMG_cell30),
                        findViewById(R.id.panel_IMG_cell31),
                        findViewById(R.id.panel_IMG_cell32),
                        findViewById(R.id.panel_IMG_cell33),
                        findViewById(R.id.panel_IMG_cell34)
                },
                new ImageView[] {
                        findViewById(R.id.panel_IMG_cell40),
                        findViewById(R.id.panel_IMG_cell41),
                        findViewById(R.id.panel_IMG_cell42),
                        findViewById(R.id.panel_IMG_cell43),
                        findViewById(R.id.panel_IMG_cell44)
                },
                new ImageView[] {
                        findViewById(R.id.panel_IMG_cell50),
                        findViewById(R.id.panel_IMG_cell51),
                        findViewById(R.id.panel_IMG_cell52),
                        findViewById(R.id.panel_IMG_cell53),
                        findViewById(R.id.panel_IMG_cell54)
                },
                new ImageView[] {
                        findViewById(R.id.panel_IMG_cell60),
                        findViewById(R.id.panel_IMG_cell61),
                        findViewById(R.id.panel_IMG_cell62),
                        findViewById(R.id.panel_IMG_cell63),
                        findViewById(R.id.panel_IMG_cell64)
                }
        };

        panel_ICN_hearts = new ImageView[] {
                findViewById(R.id.panel_ICN_heart1),
                findViewById(R.id.panel_ICN_heart2),
                findViewById(R.id.panel_ICN_heart3)
        };

    }

}

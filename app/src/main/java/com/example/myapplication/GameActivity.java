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

    private static Vibrator vibrator;
    private static Sensor accSensor;
    private static SensorManager sensorManager;

    // Panel objects
    private static ImageButton panel_BTN_left;
    private static ImageButton panel_BTN_right;
    private static ImageView[][] panel_IMG_matrix;
    private static ImageView[] panel_ICN_hearts;
    private static TextView panel_TXT_score;

    // Bundle objects
    private static Bundle bundle;
    private static boolean vibratorFlag;
    private static double lat = 0;
    private static double lng = 0;

    public static final String LAT = "LAT";
    public static final String LNG = "LNG";
    public static final String VIBRATOR_FLAG = "VIBRATOR_FLAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViews();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initSensor();

        unpackBundle();
    }


    @Override
    protected void onStart() {
        super.onStart();
        GameManager.getInstance().setActivity(this);
        GameManager.getInstance().initGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameManager.getInstance().startGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GameManager.getInstance().stopGame();
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

    public static ImageButton getPanel_BTN_left() {
        return panel_BTN_left;
    }

    public static ImageButton getPanel_BTN_right() {
        return panel_BTN_right;
    }

    public static Vibrator getVibrator() {
        return vibrator;
    }

    public static boolean isVibratorFlag() {
        return vibratorFlag;
    }

    public static ImageView[][] getPanel_IMG_matrix() {
        return panel_IMG_matrix;
    }

    public static ImageView[] getPanel_ICN_hearts() {
        return panel_ICN_hearts;
    }

    public static TextView getPanel_TXT_score() {
        return panel_TXT_score;
    }

    public static double getLat() {
        return lat;
    }

    public static double getLng() {
        return lng;
    }

    public static Bundle getBundle() {
        return bundle;
    }

    public static Sensor getAccSensor() {
        return accSensor;
    }

    public static SensorManager getSensorManager() {
        return sensorManager;
    }

    private void unpackBundle() {
        bundle = getIntent().getBundleExtra(MainActivity.BUNDLE);
        vibratorFlag = bundle.getBoolean(VIBRATOR_FLAG);
        lat = bundle.getDouble(LAT);
        lng = bundle.getDouble(LNG);
    }
}
package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameView {

    // CONSTANTS
    private final int EMPTY = 0;
    private final int PLAYER = 1;
    private final int BLOCK = 2;
    private final int CIRCLE = 3;
    private final int TRIANGLE = 4;
    private final int STAR = 5;
    private final int SQUARE = 6;
    private final int COINS = 7;
    private final int COIN_01 = 8;
    private final int COIN_05 = 9;
    private final int COIN_10 = 10;

    private final int ROWS = 7;
    private final int COLS = 5;

    // Sensors and vibrator
    private Vibrator vibrator;
    private Sensor accSensor;
    private SensorManager sensorManager;

    // Helping variables
    private int cells[][] = new int[ROWS][COLS];
    private int score, lastPosition, playerPosition;
    private String lastScoreText = "Score: 0";

    // Panel objects
    private ImageButton panel_BTN_left;
    private ImageButton panel_BTN_right;
    private ImageView[][] panel_IMG_matrix;
    private ImageView[] panel_ICN_hearts;
    private TextView panel_TXT_score;

    // Game Activity
    private AppCompatActivity activity;

    public GameView(
            AppCompatActivity activity,
            ImageButton panel_BTN_left,
            ImageButton panel_BTN_right,
            ImageView[][] panel_IMG_matrix,
            ImageView[] panel_ICN_hearts,
            TextView panel_TXT_score) {
        this.activity = activity;
        this.panel_BTN_left = panel_BTN_left;
        this.panel_BTN_right = panel_BTN_right;
        this.panel_IMG_matrix = panel_IMG_matrix;
        this.panel_ICN_hearts = panel_ICN_hearts;
        this.panel_TXT_score = panel_TXT_score;
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public Sensor getAccSensor() {
        return accSensor;
    }

    public void addButtonsListeners(
            View.OnClickListener leftButtonListener,
            View.OnClickListener rightButtonListener) {
        this.panel_BTN_left.setOnClickListener(leftButtonListener);
        this.panel_BTN_right.setOnClickListener(rightButtonListener);
    }

    public void initSensors() {
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void updateView(int[][] cells, int score, int collisionsCounter) {
        this.cells = cells;
        this.score = score;

        if (collisionsCounter > 0 && collisionsCounter < panel_ICN_hearts.length) {
            if (panel_ICN_hearts[collisionsCounter - 1].getVisibility() == View.VISIBLE) {
                removeHeart(collisionsCounter);
            }
        }

        activity.runOnUiThread(updateUI);
    }

    public void removeHeart(int collisionsCounter) {
        panel_ICN_hearts[collisionsCounter - 1].setVisibility(View.INVISIBLE);
    }

    public void updatePlayerPosition(int lastPosition, int playerPosition) {
        this.lastPosition = lastPosition;
        this.playerPosition = playerPosition;

        activity.runOnUiThread(updatePlayerPosition);
    }

    private Runnable updateUI = () -> {
        updateScoreLabel();
        updateBlocksImages();
    };

    private Runnable updatePlayerPosition = new Runnable() {
        public void run() {
            panel_IMG_matrix[ROWS - 1][lastPosition]
                    .setImageResource(0);
            panel_IMG_matrix[ROWS - 1][playerPosition]
                    .setImageResource(R.drawable.img_squid);
        }
    };

    private void updateBlocksImages() {
        // Check every cell in matrix and update picture
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                switch (cells[i][j]) {
                    case EMPTY:
                        panel_IMG_matrix[i][j]
                                .setImageResource(0);
                        break;
                    case PLAYER:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_squid);
                        break;
                    case CIRCLE:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_circle);
                        break;
                    case TRIANGLE:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_triangle);
                        break;
                    case STAR:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_star);
                        break;
                    case SQUARE:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_square);
                        break;
                    case COIN_01:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_coin_01);
                        break;
                    case COIN_05:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_coin_05);
                        break;
                    case COIN_10:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_coin_10);
                        break;

                    default:
                        Log.d(
                                "GameView",
                                "updateUI() - default: cell " + i + "," + j);
                        break;
                }
            }
        }
    }

    private void updateScoreLabel() {
        String update = "Score: " + score;
        if (!update.equals(lastScoreText)){
            panel_TXT_score.setText(update);
            lastScoreText = update;
        }
    }

    public void openActivity(Class c) {
        Intent intent = new Intent(activity.getApplicationContext(), c);
        // Add bundle to intent
        intent.putExtra(MainActivity.BUNDLE, GameActivity.getBundle());
        activity.startActivity(intent);
    }

    public void concealButtons() {
        panel_BTN_left.setVisibility(View.INVISIBLE);
        panel_BTN_right.setVisibility(View.INVISIBLE);
    }
}

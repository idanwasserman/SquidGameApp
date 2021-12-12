package com.example.myapplication.gameMVC;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.activities.GameActivity;
import com.example.myapplication.R;
import com.example.myapplication.objects.Constants;

public class GameView {

    private Vibrator vibrator;
    private Sensor accSensor;
    private SensorManager sensorManager;
    private MediaPlayer crashSound;

    // Helping variables
    private int[][] cells = new int[Constants.ROWS][Constants.COLS];
    private int score, lastPosition, playerPosition;
    private String lastScoreText = "Score: 0";
    private final boolean vibratorFlag;

    // Panel objects
    private final ImageButton panel_BTN_left;
    private final ImageButton panel_BTN_right;
    private final ImageView[][] panel_IMG_matrix;
    private final ImageView[] panel_ICN_hearts;
    private final TextView panel_TXT_score;

    // Game Activity
    private final AppCompatActivity activity;

    public GameView(
            AppCompatActivity activity,
            ImageButton panel_BTN_left,
            ImageButton panel_BTN_right,
            ImageView[][] panel_IMG_matrix,
            ImageView[] panel_ICN_hearts,
            TextView panel_TXT_score,
            boolean vibratorFlag) {
        this.activity = activity;
        this.panel_BTN_left = panel_BTN_left;
        this.panel_BTN_right = panel_BTN_right;
        this.panel_IMG_matrix = panel_IMG_matrix;
        this.panel_ICN_hearts = panel_ICN_hearts;
        this.panel_TXT_score = panel_TXT_score;
        this.vibratorFlag = vibratorFlag;

        if (vibratorFlag) {
            vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        }
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
        if (vibratorFlag) {
            vibrator.vibrate(Constants.VIBRATION_TIME);
        }
        playCrashSound();
    }

    public void updatePlayerPosition(int lastPosition, int playerPosition) {
        this.lastPosition = lastPosition;
        this.playerPosition = playerPosition;

        activity.runOnUiThread(updatePlayerPosition);
    }

    private final Runnable updateUI = () -> {
        updateScoreLabel();
        updateBlocksImages();
    };

    private final Runnable updatePlayerPosition = new Runnable() {
        public void run() {
            panel_IMG_matrix[Constants.ROWS - 1][lastPosition]
                    .setImageResource(0);
            panel_IMG_matrix[Constants.ROWS - 1][playerPosition]
                    .setImageResource(R.drawable.img_squid);
        }
    };

    private void updateBlocksImages() {
        // Check every cell in matrix and update picture
        for (int i = 0; i < Constants.ROWS; i++) {
            for (int j = 0; j < Constants.COLS; j++) {

                switch (cells[i][j]) {
                    case Constants.EMPTY:
                        panel_IMG_matrix[i][j]
                                .setImageResource(0);
                        break;
                    case Constants.PLAYER:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_squid);
                        break;
                    case Constants.CIRCLE:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_circle);
                        break;
                    case Constants.TRIANGLE:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_triangle);
                        break;
                    case Constants.STAR:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_star);
                        break;
                    case Constants.SQUARE:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_square);
                        break;
                    case Constants.COIN_01:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_coin_01);
                        break;
                    case Constants.COIN_05:
                        panel_IMG_matrix[i][j]
                                .setImageResource(R.drawable.img_coin_05);
                        break;
                    case Constants.COIN_10:
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
        intent.putExtra(Constants.BUNDLE, GameActivity.getBundle());
        activity.startActivity(intent);
    }

    public void concealButtons() {
        panel_BTN_left.setVisibility(View.INVISIBLE);
        panel_BTN_right.setVisibility(View.INVISIBLE);
    }

    public void playCrashSound() {
        if (crashSound == null) {
            crashSound = MediaPlayer.create(activity, R.raw.crash_sound);
            crashSound.setOnCompletionListener(mp -> {
                // Do nothing
            });
        }
        crashSound.start();
    }
}

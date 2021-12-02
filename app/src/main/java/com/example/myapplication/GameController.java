package com.example.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;

public class GameController {

    private Handler gameHandler;
    private GameView theView;
    private GameModel theModel;

    // Sensors and vibrator
    private static Vibrator vibrator;
    private static Sensor accSensor;
    private static SensorManager sensorManager;

    //
    private boolean sensorsFlag;

    public GameController(GameView theView, GameModel theModel, boolean sensorsFlag) {
        gameHandler = new Handler();
        this.theView = theView;
        this.theModel = theModel;
        this.sensorsFlag = sensorsFlag;

        if (sensorsFlag) {
            theView.initSensors();
//            theView.concealButtons();
            // TODO: delete line below, uncomment line above, implement moving by sensors
            theView.addButtonsListeners(leftButtonListener, rightButtonListener);
        } else {
            theView.addButtonsListeners(leftButtonListener, rightButtonListener);
        }
    }

    private Runnable runnableMethod = new Runnable() {
        @Override
        public void run() {
            theModel.timerMethod();
            theView.updateView(theModel.getCells(), theModel.getScore(), theModel.getCollisionsCounter());

            if (theModel.isGameOver()) {
                stopGame();
                theView.openActivity(TopTenActivity.class);
            }

            gameHandler.postDelayed(runnableMethod, theModel.getPeriod());
        }
    };

    private final View.OnClickListener leftButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            movePlayer(theModel.LEFT);
        }
    };

    private final View.OnClickListener rightButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            movePlayer(theModel.RIGHT);
        }
    };

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            int x, y, z;
            x = (int) event.values[0];
            y = (int) event.values[1];
            z = (int) event.values[2];

            // TODO: Check x : if changed --> change position

            theModel.updatePeriodByZ(z);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    };

    private void movePlayer(int direction) {
        int lastPosition = theModel.getPlayerPosition();
        if (theModel.canMove(direction)) {
            theView.updatePlayerPosition(
                    lastPosition,
                    lastPosition + direction);
            if (theModel.movePlayer(direction)) {
                theView.removeHeart(theModel.getCollisionsCounter());
            }
        }
    }

    public void initGame() {
        theModel.initGame();
    }

    public void startGame() {
        if (!theModel.isGameOver()) {
            if (sensorsFlag) {
                theView.getSensorManager().registerListener(sensorEventListener, theView.getAccSensor(), SensorManager.SENSOR_DELAY_NORMAL);
            }
            runnableMethod.run();
        }
    }

    public void stopGame() {
        gameHandler.removeCallbacks(runnableMethod);
        if (sensorsFlag) {
            theView.getSensorManager().unregisterListener(sensorEventListener, theView.getAccSensor());
        }
    }

}

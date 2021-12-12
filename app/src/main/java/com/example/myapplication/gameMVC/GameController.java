package com.example.myapplication.gameMVC;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;

import com.example.myapplication.activities.TopTenActivity;

public class GameController {

    private Handler gameHandler;
    private GameView theView;
    private GameModel theModel;

    // Sensors and vibrator
    private static Vibrator vibrator;
    private static Sensor accSensor;
    private static SensorManager sensorManager;

    private static final int AXIS_X = 0;
    private static final int AXIS_Z = 2;
    private static final int TURN_ANGLE = 3;
    private static final long DELAY = 200000000;
    private long currEventTimestamp = 0, prevEventTimestamp = 0;
    private boolean sensorsFlag;

    public GameController(GameView theView, GameModel theModel, boolean sensorsFlag) {
        gameHandler = new Handler();
        this.theView = theView;
        this.theModel = theModel;
        this.sensorsFlag = sensorsFlag;

        if (sensorsFlag) {
            theView.initSensors();
            theView.concealButtons();
            // TODO: delete line below, uncomment line above, implement moving by sensors
            //theView.addButtonsListeners(leftButtonListener, rightButtonListener);
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
                theView.removeHeart(theModel.getCollisionsCounter());
                new Handler().postDelayed(() ->
                        theView.openActivity(TopTenActivity.class), 750);
            }

            gameHandler.postDelayed(runnableMethod, theModel.getPeriod());
        }
    };

    private final View.OnClickListener leftButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            movePlayer(Constants.LEFT);
        }
    };

    private final View.OnClickListener rightButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            movePlayer(Constants.RIGHT);
        }
    };


    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            currEventTimestamp = event.timestamp;
            int x = (int) event.values[AXIS_X];
            int z = (int) event.values[AXIS_Z];

            theModel.updatePeriodByZ(z);

            if (currEventTimestamp - prevEventTimestamp > DELAY) {
                prevEventTimestamp = currEventTimestamp;
                if (x >= TURN_ANGLE) {
                    movePlayer(Constants.LEFT);
                } else if (x <= -TURN_ANGLE) {
                    movePlayer(Constants.RIGHT);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    };

    private void movePlayer(int direction) {
        int lastPosition = theModel.getPlayerPosition();
        int playerPosition = lastPosition + direction;
        if (theModel.canMove(direction)) {
            theView.updatePlayerPosition(
                    lastPosition,
                    playerPosition);
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

package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.objects.MyDatabase;
import com.example.myapplication.objects.Record;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    // CONSTANTS
    private final int EMPTY = 0;
    private final int PLAYER = 1;
    private final int BLOCK = 2;
    private final int CIRCLE = 3;
    private final int TRIANGLE = 4;
    private final int STAR = 5;
    private final int UMBRELLA = 6;
    private final int COINS = 7;
    private final int COIN_01 = 8;
    private final int COIN_05 = 9;
    private final int COIN_10 = 10;

    private final int NUM_OF_BLOCKS = 4;
    private final int NUM_OF_COINS = 3;
    private final int COIN_CHANCE = 3;

    private final int ROWS = 7;
    private final int COLS = 5;
    private final int LIVES = 3;

    public static final String LAT = "LAT";
    public static final String LNG = "LNG";
    public static final String VIBRATOR_FLAG = "VIBRATOR_FLAG";
    private final String MY_DB_NAME = "SQUID_GAME_DB";
    private final String defDbVal = "{\"records\":[]}";

    private final int MIN_PERIOD = 300;
    private final int MAX_PERIOD = 1070;
    private final int Z_SPACES = 11;
    private final int Z_AVG_CHANGE = (MAX_PERIOD - MIN_PERIOD) / Z_SPACES;
    private int period = MAX_PERIOD;

    // Logical variables
    private int playerPosition = 1;
    private int collisionsCounter = 0;
    private int cells[][] = new int[ROWS][COLS];
    private int counter = 0;
    private boolean gameOver = false;
    private int score = 0;
    private boolean scoreChangedFlag = false;
    private boolean xChanged = false;
    private int lastX, lastZ = 0;

    private Handler timerHandler = new Handler();
    private Vibrator vibrator;
    private Sensor accSensor;
    private SensorManager sensorManager;

    // Panel objects
    private ImageButton panel_BTN_left;
    private ImageButton panel_BTN_right;
    private ImageView[][] panel_IMG_matrix;
    private ImageView[] panel_ICN_hearts;
    private TextView panel_TXT_score;

    // Bundle objects
    private Bundle bundle;
    private boolean vibratorFlag;
    private double lat = 0, lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViews();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initSensor();

        // Unpack bundle objects
        bundle = getIntent().getBundleExtra(MainActivity.BUNDLE);
        vibratorFlag = bundle.getBoolean(VIBRATOR_FLAG);
        lat = bundle.getDouble(LAT);
        lng = bundle.getDouble(LNG);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // init cells of matrix to be EMPTY
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j] = EMPTY;
            }
        }
        cells[ROWS - 1][playerPosition] = PLAYER; // player's starting position cell

        // LEFT button
        panel_BTN_left.setOnClickListener(leftButtonListener);

        // RIGHT button
        panel_BTN_right.setOnClickListener(rightButtonListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // If game is not over -> run timer method
        if (!gameOver) {
            runnableTimerMethod.run();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener, accSensor);
        // If user doesn't focus on game -> cancel timer handler
        timerHandler.removeCallbacks(runnableTimerMethod);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            int x, y, z;
            x = (int) event.values[0];
            y = (int) event.values[1];
            z = (int) event.values[2];

            // TODO: Check x : if changed --> change position

            updatePeriodByZ(z);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void updatePeriodByZ(int z) {
        if ((z >= 0 && z <= 10) && (z != lastZ)) {
            period = MAX_PERIOD - z * Z_AVG_CHANGE;
            lastZ = z;
        }
    }

    private View.OnClickListener leftButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (playerPosition == 0)
                return;

            if (playerPosition > 0) {
                if (isPlayerCollide(ROWS - 1, playerPosition - 1)) {
                    runOnUiThread(handleCollision);
                }
            }

            panel_IMG_matrix[ROWS - 1][playerPosition].setImageResource(0);
            panel_IMG_matrix[ROWS - 1][--playerPosition].setImageResource(R.drawable.img_squid); // Position decreased by 1
        }
    };

    private View.OnClickListener rightButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (playerPosition == COLS - 1)
                return;

            if (playerPosition < COLS - 1) {
                if (isPlayerCollide(ROWS - 1, playerPosition + 1)) {
                    runOnUiThread(handleCollision);
                }
            }

            panel_IMG_matrix[ROWS - 1][playerPosition].setImageResource(0);
            panel_IMG_matrix[ROWS - 1][++playerPosition].setImageResource(R.drawable.img_squid); // Position increased by 1
        }
    };

    // Check if player is going to collide a block
    // if not -> check if player is getting a coin and update score
    private boolean isPlayerCollide(int rows, int cols) {
        if (cells[rows][cols] >= BLOCK && cells[rows][cols] < COINS) {
            return true;
        } else {
            switch (cells[rows][cols]) {
                case COIN_01:
                    score += 1;
                    break;
                case COIN_05:
                    score += 5;
                    break;
                case COIN_10:
                    score += 10;
                    break;
            }
        }
        return false;
    }

    private void TimerMethod() {
        // If there is a block one cell above the player -> collision
        if (isPlayerCollide(ROWS - 2, playerPosition)) {
            this.runOnUiThread(handleCollision);
        }

        moveBlocksOneRowDown();

        addScoreIfPassedBlocks();

        // Player position in cells may have been overridden
        cells[ROWS - 1][playerPosition] = PLAYER;

        emptyFirstRow();

        randomNewBlock();

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(updateUI);
    }

    private void randomNewBlock() {
        if (counter % 2 == 0 || counter % 3 == 0 || counter % 5 == 0) {
            int randomColumn = new Random().nextInt(COLS);
            int randomBlockImage = new Random().nextInt(NUM_OF_BLOCKS) + BLOCK + 1;
            cells[0][randomColumn] = randomBlockImage;

            // random if need to add a coin to the line
            int addCoin = new Random().nextInt(COIN_CHANCE);
            if (addCoin == 0) {
                int randomCoin = new Random().nextInt(NUM_OF_COINS) + COINS + 1;
                int randomCoinColumn = new Random().nextInt(COLS);
                if (randomCoinColumn == randomColumn) {
                    randomCoinColumn++;
                    randomCoinColumn %= COLS;
                }
                cells[0][randomCoinColumn] = randomCoin;
            }
        }

        counter++;
    }

    private void emptyFirstRow() {
        for (int i = 0; i < COLS; i++) {
            cells[0][i] = EMPTY;
        }
    }

    private void addScoreIfPassedBlocks() {
        for (int i = 0; i < COLS; i++) {
            if (cells[ROWS - 1][i] >= BLOCK) {
                score += 10;
                scoreChangedFlag = true;
            }
        }
    }

    private void moveBlocksOneRowDown() {
        for (int i = ROWS - 1; i > 0; i--) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j] = cells[i - 1][j];
            }
        }
    }

    private Runnable runnableTimerMethod = new Runnable() {
        @Override
        public void run() {
            TimerMethod();
            timerHandler.postDelayed(runnableTimerMethod, period);
        }
    };

    private Runnable handleCollision = new Runnable() {
        public void run() {
            // Vibrate when collide a block
            if (vibratorFlag) {
                vibrator.vibrate(750);
            }

            // Remove one heart
            if (collisionsCounter < panel_ICN_hearts.length) {
                panel_ICN_hearts[collisionsCounter++].setVisibility(View.INVISIBLE); // Collisions counter increased by 1
            }
            Log.d("GameActivity", "Collision #" + collisionsCounter);

            // Check if game is over
            if (collisionsCounter == LIVES) {
                endGame();
            }
        }
    };

    private void endGame() {
        Log.d("GameActivity", "GAME OVER!");
        gameOver = true;

        // Longer vibrate for game over
        if (vibratorFlag) {
            vibrator.vibrate(800);
        }

        // Cancel timer handler
        timerHandler.removeCallbacks(runnableTimerMethod);

        // Create game record and update the database
        updateDatabase();

        // Open top ten activity after game is over
        openActivity(TopTenActivity.class);
    }

    private void updateDatabase() {
        // Fetch database as json
        String str_db = MySharedPreferences
                .getInstance()
                .getString(
                        MY_DB_NAME, // key
                        defDbVal    // def value
                );
        // Convert json to object
        MyDatabase my_db = new Gson()
                .fromJson(
                        str_db,
                        MyDatabase.class
                );

        // Create a record and store it in my_db
        Record record = new Record()
                .setDate(new Date())
                .setLat(lat)
                .setLng(lng)
                .setScore(score);

//        cleanRecordsFromDatabase(my_db);
        my_db.getRecords().add(record);

        // Store my_db in app shared preferences
        String json = new Gson().toJson(my_db);
        MySharedPreferences
                .getInstance()
                .putString(
                        MY_DB_NAME,
                        json
                );
    }

    private void cleanRecordsFromDatabase(MyDatabase my_db) {
        my_db.setRecords(new ArrayList<Record>());
    }

    private void openActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        // Add bundle to intent
        intent.putExtra("BUNDLE", bundle);
        startActivity(intent);
    }

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private Runnable updateUI = new Runnable() {
        public void run() {
            //This method runs in the same thread as the UI.
            updateScoreLabel();
            updateBlocksImages();
        }
    };

    private void updateBlocksImages() {
        // Check every cell in matrix and update picture
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                switch (cells[i][j]) {
                    case EMPTY:
                        panel_IMG_matrix[i][j].setImageResource(0);
                        break;
                    case PLAYER:
                        panel_IMG_matrix[i][j].setImageResource(R.drawable.img_squid);
                        break;
                    case CIRCLE:
                        panel_IMG_matrix[i][j].setImageResource(R.drawable.img_circle);
                        break;
                    case TRIANGLE:
                        panel_IMG_matrix[i][j].setImageResource(R.drawable.img_triangle);
                        break;
                    case STAR:
                        panel_IMG_matrix[i][j].setImageResource(R.drawable.img_star);
                        break;
                    case UMBRELLA:
                        panel_IMG_matrix[i][j].setImageResource(R.drawable.img_umbrella);
                        break;
                    case COIN_01:
                        panel_IMG_matrix[i][j].setImageResource(R.drawable.img_coin_01);
                        break;
                    case COIN_05:
                        panel_IMG_matrix[i][j].setImageResource(R.drawable.img_coin_05);
                        break;
                    case COIN_10:
                        panel_IMG_matrix[i][j].setImageResource(R.drawable.img_coin_10);
                        break;

                    default:
                        Log.d("GameActivity", "updateUI() - default: cell " + i + "," + j);
                        break;
                }
            }
        }
    }

    private void updateScoreLabel() {
        if (!gameOver && scoreChangedFlag) {
            scoreChangedFlag = false;
            panel_TXT_score.setText("Score: " + score);
        }
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
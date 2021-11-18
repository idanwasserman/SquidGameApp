package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

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

    private int PERIOD = 1000;
    private final int MIN_PERIOD = 350;
    private final int ROWS = 7, COLS = 5;
    private final int LIVES = 3;


    // Logical variables
    private int playerPosition = 1;
    private int collisionsCounter = 0;
    private int cells[][] = new int[ROWS][COLS];
    private int counter = 0;
    private boolean gameOver = false;
    private int score = 0;
    private boolean scoreChangedFlag = false;

    private Timer timer;
    private Vibrator v;

    // Panel objects
    private ImageButton panel_BTN_left;
    private ImageButton panel_BTN_right;
    private ImageView[][] panel_IMG_matrix;
    private ImageView[] panel_ICN_hearts;
    private TextView panel_TXT_score;
    private RelativeLayout panel_RL_gameOverMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // init cells of matrix to be EMPTY
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j] = EMPTY;
            }
        }
        cells[ROWS - 1][playerPosition] = PLAYER; // player's starting position cell
    }

    @Override
    protected void onStart() {
        super.onStart();

        // LEFT button
        panel_BTN_left.setOnClickListener(new View.OnClickListener() {
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
        });

        // RIGHT button
        panel_BTN_right.setOnClickListener(new View.OnClickListener() {
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
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If game is not over -> start timer
        if (!gameOver)
        {
            // TIMER
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    TimerMethod();
                }
            }, 0, PERIOD);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        // If user doesn't focus on game -> cancel timer
        timer.cancel();
    }


    // check if player is going to collide a block
    // if not -> check if player is getting a coin and update score
    private boolean isPlayerCollide(int rows, int cols) {
        if (cells[rows][cols] >= BLOCK && cells[rows][cols] < COINS) {
            return true;
        } else {
            if (cells[rows][cols] == COIN_01) {
                score += 1;
            } else if (cells[rows][cols] == COIN_05) {
                score += 5;
            } else if (cells[rows][cols] == COIN_10) {
                score += 10;
            }
        }
        return false;
    }

    private void TimerMethod() {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        // If there is a block one cell above the player -> collision
        if (isPlayerCollide(ROWS - 2, playerPosition)) {
            this.runOnUiThread(handleCollision);
        }

        // Move all the blocks one row down
        for (int i = ROWS - 1; i > 0; i--) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j] = cells[i - 1][j];
            }
        }

        // Add score points if last row contains blocks
        for (int i = 0; i < COLS; i++) {
            if (cells[ROWS - 1][i] >= BLOCK) {
                score += 10;
                scoreChangedFlag = true;
            }
        }

        // Player position in cells may have been overridden
        cells[ROWS - 1][playerPosition] = PLAYER;

        // Empty first row
        for (int i = 0; i < COLS; i++) {
            cells[0][i] = EMPTY;
        }

        // Random new block every other timer tick
        if (counter % 2 == 0 || counter % 3 == 0) {
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




        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(updateUI);
    }

    private Runnable handleCollision = new Runnable() {
        public void run() {
            // Vibrate when collide a block
            v.vibrate(500);

            // Remove one heart
            if (collisionsCounter < panel_ICN_hearts.length) {
                panel_ICN_hearts[collisionsCounter++].setVisibility(View.INVISIBLE); // Collisions counter increased by 1
            }
            Log.d("tag", "Collision #" + collisionsCounter);

            // Check if game is over
            if (collisionsCounter == LIVES) {
                v.vibrate(500);
                gameOver = true;
                Log.d("d", "GAME OVER!");
                timer.cancel();

                // Show message
                panel_RL_gameOverMessage.setVisibility(View.VISIBLE);
            }
        }
    };

    private Runnable updateUI = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.

            // Change score
            if (scoreChangedFlag) {
                scoreChangedFlag = false;
                panel_TXT_score.setText("Score: " + score);
            }

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
                            Log.d("d", "default: cell " + i + "," + j);
                            break;
                    }
                }
            }
        }
    };

    private void findViews() {

        panel_BTN_left = findViewById(R.id.panel_BTN_left);
        panel_BTN_right = findViewById(R.id.panel_BTN_right);

        panel_TXT_score = findViewById(R.id.panel_TXT_score);

        panel_RL_gameOverMessage = findViewById(R.id.panel_RL_gameOverMessage);

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

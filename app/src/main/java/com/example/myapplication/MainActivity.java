package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

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
    private final int NUM_OF_BLOCKS = 4;

    private final int PERIOD = 1000;
    private final int ROWS = 4, COLS = 3;
    private final int LIVES = 3;


    // Logical variables
    private int playerPosition = 1, collisionsCounter = 0;
    private int cells[][] = new int[ROWS][COLS];
    private int counter = 0;
    private boolean gameOver = false;

    private Timer timer;
    private Vibrator v;

    // Panel objects
    private ImageButton panel_BTN_left;
    private ImageButton panel_BTN_right;
    private ImageView[][] panel_IMG_matrix;
    private ImageView[] panel_ICN_hearts;


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
                    if (cells[ROWS - 1][playerPosition - 1] >= BLOCK) {
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
                    if (cells[ROWS - 1][playerPosition + 1] >= BLOCK) {
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

    private void TimerMethod() {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        // If there is a block one cell above the player -> collision
        if (cells[ROWS - 2][playerPosition] >= BLOCK) {
            this.runOnUiThread(handleCollision);
        }

        // Move all the blocks one row down
        for (int i = ROWS - 1; i > 0; i--) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j] = cells[i - 1][j];
            }
        }

        // Player position in cells may have been overridden
        cells[ROWS - 1][playerPosition] = PLAYER;

        // Empty first row
        for (int i = 0; i < COLS; i++) {
            cells[0][i] = EMPTY;
        }

        // Random new block every other timer tick
        if (counter++ % 2 == 0) {
            int randomColumn = new Random().nextInt(COLS);
            int randomBlockImage = new Random().nextInt(NUM_OF_BLOCKS) + BLOCK + 1;
            cells[0][randomColumn] = randomBlockImage;
        }


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
            }
        }
    };

    private Runnable updateUI = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.

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

        panel_IMG_matrix = new ImageView[][] {
                new ImageView[] {
                    findViewById(R.id.panel_IMG_cell00),
                    findViewById(R.id.panel_IMG_cell01),
                    findViewById(R.id.panel_IMG_cell02),
                },
                new ImageView[] {
                        findViewById(R.id.panel_IMG_cell10),
                        findViewById(R.id.panel_IMG_cell11),
                        findViewById(R.id.panel_IMG_cell12),
                },
                new ImageView[] {
                        findViewById(R.id.panel_IMG_cell20),
                        findViewById(R.id.panel_IMG_cell21),
                        findViewById(R.id.panel_IMG_cell22),
                },
                new ImageView[] {
                        findViewById(R.id.panel_IMG_cell30),
                        findViewById(R.id.panel_IMG_cell31),
                        findViewById(R.id.panel_IMG_cell32),
                }
        };

        panel_ICN_hearts = new ImageView[] {
                findViewById(R.id.panel_ICN_heart1),
                findViewById(R.id.panel_ICN_heart2),
                findViewById(R.id.panel_ICN_heart3)
        };

    }
}

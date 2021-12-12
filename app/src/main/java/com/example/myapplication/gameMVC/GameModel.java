package com.example.myapplication.gameMVC;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.objects.MySharedPreferences;
import com.example.myapplication.objects.MyDatabase;
import com.example.myapplication.objects.Record;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class GameModel {

    private int period = Constants.MAX_PERIOD;

    private int playerPosition = Constants.COLS / 2;
    private int collisionsCounter = 0;
    private int cells[][] = new int[Constants.ROWS][Constants.COLS];
    private int counter = 0;
    private boolean gameOver = false;
    private int score = 0;
    private int lastZ = 0;

    private String nickname;
    private double lat, lng;
    private boolean sensorsMode;

    public GameModel(String nickname, double lat, double lng, boolean sensorsMode) {
        this.nickname = nickname;
        this.lat = lat;
        this.lng = lng;
        this.sensorsMode = sensorsMode;
    }

    public int getPeriod() {
        return period;
    }

    public int[][] getCells() {
        return cells;
    }

    public int getCollisionsCounter() {
        return collisionsCounter;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getScore() {
        return score;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

    public void initGame() {
        // init cells of matrix to be EMPTY
        for (int i = 0; i < Constants.ROWS; i++) {
            for (int j = 0; j < Constants.COLS; j++) {
                cells[i][j] = Constants.EMPTY;
            }
        }

        // Player's starting position cell
        cells[Constants.ROWS - 1][playerPosition] = Constants.PLAYER;
    }

    /**
     *
     * @param direction left or right
     *                 the function checks if the player has where to move
     *                 if so -> checks player doesn't collide a block
     * @return boolean value which indicates there was a collision
     */
    public boolean movePlayer(int direction) {
        playerPosition += direction;

        if (isPlayerCollide(Constants.ROWS - 1, playerPosition)) {
            handlePlayerCollision();
            return true;
        }
        return false;
    }

    private void handlePlayerCollision() {
        collisionsCounter++;
        Log.d("GameModel", "Collision #" + collisionsCounter);

        if (collisionsCounter >= Constants.NUM_OF_LIVES) {
            finishGame();
        }
    }

    private void finishGame() {
        gameOver = true;
        Log.d("GameModel", "Game Over!");

        // Create game record and update the database
        updateDatabase();
    }

    /**
     *
     * @param row player's current row
     * @param col player's current column
     * @return is player going to collide a block
     * if not -> function checks if player collected a coin
     * and updates score
     */
    private boolean isPlayerCollide(int row, int col) {
        if (cells[row][col] >= Constants.BLOCK
                && cells[row][col] < Constants.COINS) {
            return true;
        } else {
            switch (cells[row][col]) {
                case Constants.COIN_01:
                    score += 1;
                    break;
                case Constants.COIN_05:
                    score += 5;
                    break;
                case Constants.COIN_10:
                    score += 10;
                    break;
            }
        }
        return false;
    }

    private void updateDatabase() {
        // Fetch database as json
        String str_db = MySharedPreferences
                .getInstance()
                .getString(
                        Constants.MY_DB_NAME, // key
                        Constants.DEFAULT_DB_VALUE    // def value
                );
        // Convert json to object
        MyDatabase my_db = new Gson()
                .fromJson(
                        str_db,
                        MyDatabase.class
                );

        DateFormat date = new SimpleDateFormat(Constants.PATTERN);
        String dateFormat = date.format(Calendar.getInstance().getTime());
        // Create a record and store it in my_db
        Record record = new Record()
                .setDateFormat(dateFormat)
                .setNickname(nickname)
                .setLat(lat)
                .setLng(lng)
                .setSensorsMode(sensorsMode)
                .setScore(score);

        cleanRecordsBelowOneThousandScore(my_db);
        my_db.getRecords().add(record);

        // Store my_db in app shared preferences
        String json = new Gson().toJson(my_db);
        MySharedPreferences
                .getInstance()
                .putString(
                        Constants.MY_DB_NAME,
                        json
                );
    }

    private void cleanRecordsBelowOneThousandScore(@NonNull MyDatabase my_db) {
        ArrayList<Record> top3 = new ArrayList<>();
        ArrayList<Record> newRecords = new ArrayList<>();
        ArrayList<Record> records = my_db.getRecords();
        for (Record r:
             records) {
            if (r.getScore() >= 400) {
                newRecords.add(r);
            }
        }
        my_db.setRecords(newRecords);
    }

    public void timerMethod() {
        // If there is a block one cell above the player -> collision
        if (isPlayerCollide(Constants.ROWS - 2, playerPosition)) {
            handlePlayerCollision();
        }

        moveBlocksOneRowDown();

        addScoreIfPassedBlocks();

        // Player position in cells may have been overridden
        cells[Constants.ROWS - 1][playerPosition] = Constants.PLAYER;

        emptyFirstRow();

        randomNewBlock();
    }

    private void randomNewBlock() {
        if (counter % 2 == 0 || counter % 3 == 0 || counter % 5 == 0) {
            int randomColumn = new Random().nextInt(Constants.COLS);
            int randomBlockImage = new Random().nextInt(Constants.NUM_OF_BLOCKS) + Constants.BLOCK + 1;
            cells[0][randomColumn] = randomBlockImage;

            // random if need to add a coin to the line
            int addCoin = new Random().nextInt(Constants.COIN_CHANCE);
            if (addCoin == 0) {
                int randomCoin = new Random().nextInt(Constants.NUM_OF_COINS) + Constants.COINS + 1;
                int randomCoinColumn = new Random().nextInt(Constants.COLS);
                if (randomCoinColumn == randomColumn) {
                    randomCoinColumn++;
                    randomCoinColumn %= Constants.COLS;
                }
                cells[0][randomCoinColumn] = randomCoin;
            }
        }

        counter++;
    }

    private void emptyFirstRow() {
        for (int i = 0; i < Constants.COLS; i++) {
            cells[0][i] = Constants.EMPTY;
        }
    }

    private void addScoreIfPassedBlocks() {
        for (int i = 0; i < Constants.COLS; i++) {
            if (cells[Constants.ROWS - 1][i] >= Constants.BLOCK) {
                score += 10;
            }
        }
    }

    private void moveBlocksOneRowDown() {
        for (int i = Constants.ROWS - 1; i > 0; i--) {
            for (int j = 0; j < Constants.COLS; j++) {
                cells[i][j] = cells[i - 1][j];
            }
        }
    }

    /**
     *
     * @param direction what way the player is trying to move to
     * @return true if player can move
     */
    public boolean canMove(int direction) {
        if (direction == Constants.LEFT) {
            if (playerPosition == 0) {
                return false;
            }
        } else if (direction == Constants.RIGHT) {
            if (playerPosition == Constants.COLS - 1) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public void updatePeriodByZ(int z) {
        if ((z >= 0 && z <= 10) && (z != lastZ)) {
            period = Constants.MAX_PERIOD - z * Constants.Z_AVG_CHANGE;
            lastZ = z;
        }
    }
}

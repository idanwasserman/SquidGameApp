package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.objects.MyDatabase;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private Button main_BTN_play;
    private Button main_BTN_topTen;
    private Button main_BTN_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setButtonListeners();
    }

    private View.OnClickListener playBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openActivity(GameActivity.class);
        }
    };

    private View.OnClickListener topTenBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openActivity(TopTenActivity.class);
        }
    };

    private View.OnClickListener exitBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO exit button
        }
    };

    private void openActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }

    private void setButtonListeners() {
        main_BTN_play.setOnClickListener(playBtnListener);
        main_BTN_topTen.setOnClickListener(topTenBtnListener);
        main_BTN_exit.setOnClickListener(exitBtnListener);
    }

    private void findViews() {
        main_BTN_play = findViewById(R.id.main_BTN_play);
        main_BTN_topTen = findViewById(R.id.main_BTN_topTen);
        main_BTN_exit = findViewById(R.id.main_BTN_exit);
    }
}

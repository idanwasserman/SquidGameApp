package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TopTenActivity extends AppCompatActivity {

    private Button topTen_BTN_mainMenu;

    private Fragment_TopTenList fragment_topTenList;
    private Fragment_Map fragment_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);

        findViews();
        topTen_BTN_mainMenu.setOnClickListener(mainMenuBtnListener);

        fragment_topTenList = new Fragment_TopTenList();
        fragment_topTenList.setActivity(this);
        // set callback
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, fragment_topTenList).commit();

        fragment_map = new Fragment_Map();
        fragment_map.setActivity(this);
        // set callback
        getSupportFragmentManager().beginTransaction().add(R.id.frame2, fragment_map).commit();
    }

    View.OnClickListener mainMenuBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openActivity(MainActivity.class);
        }
    };

    private void openActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }

    private void findViews() {
        topTen_BTN_mainMenu = findViewById(R.id.topTen_BTN_mainMenu);
    }
}
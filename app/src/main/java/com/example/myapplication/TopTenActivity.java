package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.objects.Record;
import com.google.android.gms.maps.model.LatLng;

public class TopTenActivity extends AppCompatActivity {

    private Button topTen_BTN_mainMenu;

    private Fragment_TopTenList fragment_topTenList;
    private Fragment_Map fragment_map;

//    private static final String LAT = "LAT";
//    private static final String LNG = "LNG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);

        findViews();

        // Main menu button
        topTen_BTN_mainMenu.setOnClickListener(mainMenuBtnListener);

        // Top ten list fragment
        fragment_topTenList = new Fragment_TopTenList();
        fragment_topTenList.setActivity(this);
        fragment_topTenList.setCallBack_list(callBack_list);
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, fragment_topTenList).commit();

        // Map fragment
        fragment_map = new Fragment_Map();
        fragment_map.setActivity(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame2, fragment_map).commit();

        Bundle bundle = getIntent().getBundleExtra(MainActivity.BUNDLE);
        double lat = bundle.getDouble(GameActivity.LAT);
        double lng = bundle.getDouble(GameActivity.LNG);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    View.OnClickListener mainMenuBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    CallBack_List callBack_list = new CallBack_List() {
        @Override
        public void setMapLocation(double lat, double lng) {
            fragment_map.setFocusOnMapByLocation(new LatLng(lat, lng));
        }
    };

    private void findViews() {
        topTen_BTN_mainMenu = findViewById(R.id.topTen_BTN_mainMenu);
    }
}
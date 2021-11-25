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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);

        findViews();
        topTen_BTN_mainMenu.setOnClickListener(mainMenuBtnListener);

        fragment_topTenList = new Fragment_TopTenList();
        fragment_topTenList.setActivity(this);
        fragment_topTenList.setCallBack_list(callBack_list);
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, fragment_topTenList).commit();

        fragment_map = new Fragment_Map();
        fragment_map.setActivity(this);
        //
        getSupportFragmentManager().beginTransaction().add(R.id.frame2, fragment_map).commit();
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

/*    CallBack_Map callBack_map = new CallBack_Map() {
        @Override
        public void getMapLocation(double lat, double lng) {

        }
    };*/

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
package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.interfaces.CallBack_List;
import com.example.myapplication.R;
import com.example.myapplication.fragments.Fragment_Map;
import com.example.myapplication.fragments.Fragment_TopTenList;
import com.example.myapplication.objects.Constants;
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

        // Main menu button
        topTen_BTN_mainMenu.setOnClickListener(mainMenuBtnListener);

        // Top ten list fragment
        fragment_topTenList = new Fragment_TopTenList();
        fragment_topTenList.setCallBack_list(callBack_list);
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, fragment_topTenList).commit();

        // Map fragment
        fragment_map = new Fragment_Map();
        getSupportFragmentManager().beginTransaction().add(R.id.frame2, fragment_map).commit();

        Bundle bundle = getIntent().getBundleExtra(Constants.BUNDLE);
        double lat = bundle.getDouble(GameActivity.LAT);
        double lng = bundle.getDouble(GameActivity.LNG);
        fragment_map.setDefaultLocation(new LatLng(lat, lng));
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
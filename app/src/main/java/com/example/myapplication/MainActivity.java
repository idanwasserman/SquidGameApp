package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.objects.MyDatabase;
import com.example.myapplication.objects.Record;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SettingsDialog.SettingsDialogListener {

    private Button main_BTN_play;
    private Button main_BTN_topTen;
    private Button main_BTN_settings;

    private boolean sensors = false;
    private boolean sounds = false;
    private boolean vibrator = false;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private MediaPlayer player;
    private double lat, lng;

    public static final String BUNDLE = "BUNDLE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Long time1 = System.currentTimeMillis();
        Log.d("TIME", "time1: " + time1);

        findViews();
        setButtonListeners();

        // Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Check location permission
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        Long time2 = System.currentTimeMillis();
        Log.d("TIME", "time2: " + time2);

        if (time1 > time2) {
            Log.d("TIME", "time1 > time2");
        } else {
            Log.d("TIME", "time2 > time1");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (player != null) {
            player.stop();
        }
        finish();
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

    private View.OnClickListener settingsBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openSettingsDialog();
        }
    };

    private void openSettingsDialog() {
        SettingsDialog settingsDialog = new SettingsDialog(sensors, sounds, vibrator);
        settingsDialog.show(getSupportFragmentManager(), "settings dialog");
    }

    private void openActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);

        // Pack bundle
        Bundle bundle = new Bundle();
        bundle.putBoolean(GameActivity.VIBRATOR_FLAG, vibrator);
        bundle.putDouble(GameActivity.LAT, lat);
        bundle.putDouble(GameActivity.LNG, lng);

        // Add bundle to intent
        intent.putExtra(BUNDLE, bundle);
        startActivity(intent);
    }

    private void setButtonListeners() {
        main_BTN_play.setOnClickListener(playBtnListener);
        main_BTN_topTen.setOnClickListener(topTenBtnListener);
        main_BTN_settings.setOnClickListener(settingsBtnListener);
    }

    private void findViews() {
        main_BTN_play = findViewById(R.id.main_BTN_play);
        main_BTN_topTen = findViewById(R.id.main_BTN_topTen);
        main_BTN_settings = findViewById(R.id.main_BTN_settings);
    }

    @Override
    public void applySettings(boolean sensors, boolean sounds, boolean vibrator) {
        this.sensors = sensors;
        this.sounds = sounds;
        this.vibrator = vibrator;

        if (sounds) {
            play();
        } else {
            pause();
        }
    }

    public void play() {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.squid_game_song_remix);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    play();
                }
            });
        }

        player.start();
    }

    public void pause() {
        if (player != null) {
            player.pause();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this,
                                Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        lat = addressList.get(0).getLatitude();
                        lng = addressList.get(0).getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}

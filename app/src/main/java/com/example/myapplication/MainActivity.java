package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
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
//    private LocationManager locationManager;
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setButtonListeners();

        // init fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Check location permission
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
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

        Bundle bundle = new Bundle();
        bundle.putBoolean("VIBRATOR", vibrator);
        bundle.putDouble("LAT", lat);
        bundle.putDouble("LNG", lng);

        intent.putExtra("BUNDLE", bundle);
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

        Log.d("settings", "sensors="+sensors+" , sounds="+sounds);

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
                        Log.d("GPS_TAG", "MainActivity\nlat: " + lat + "\nlng: " + lng);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}

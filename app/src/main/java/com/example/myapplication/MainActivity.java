package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SettingsDialog.SettingsDialogListener {

    private Button main_BTN_play;
    private Button main_BTN_topTen;
    private Button main_BTN_settings;

    private boolean sensors = false;
    private boolean sounds = false;
    private boolean vibrator = false;

    private MediaPlayer player;
    private LocationManager locationManager;
    private double lat=0, lng=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setButtonListeners();

        // Runtime permissions
        if (ContextCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
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
    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext()
                    .getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            Log.d("GPS", lat + " : " + lng);
        }
    };
}

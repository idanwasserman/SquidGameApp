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
import android.view.View;
import android.widget.Button;

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

    private boolean sensorsFlag = false;
    private boolean soundsFlag = false;
    private boolean vibratorFlag = false;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private MediaPlayer mediaPlayer;
    private double lat, lng;

    public static final String BUNDLE = "BUNDLE";
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        finish();
    }

    private View.OnClickListener playBtnListener = v -> openActivity(GameActivity.class);

    private View.OnClickListener topTenBtnListener = v -> openActivity(TopTenActivity.class);

    private View.OnClickListener settingsBtnListener = v -> openSettingsDialog();

    private void openSettingsDialog() {
        SettingsDialog settingsDialog = new SettingsDialog(sensorsFlag, soundsFlag, vibratorFlag);
        settingsDialog.show(getSupportFragmentManager(), "settings dialog");
    }

    private void openActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        packBundle();
        intent.putExtra(BUNDLE, bundle); // Add bundle to intent
        startActivity(intent);
    }

    private void packBundle() {
        bundle = new Bundle();
        bundle.putBoolean(GameActivity.VIBRATOR_FLAG, vibratorFlag);
        bundle.putBoolean(GameActivity.SENSORS_FLAG, sensorsFlag);
        bundle.putDouble(GameActivity.LAT, lat);
        bundle.putDouble(GameActivity.LNG, lng);
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
        this.sensorsFlag = sensors;
        this.soundsFlag = sounds;
        this.vibratorFlag = vibrator;

        if (sounds) {
            play();
        } else {
            pause();
        }
    }

    public void play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.squid_game_song_remix);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    play();
                }
            });
        }

        mediaPlayer.start();
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
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

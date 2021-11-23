package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements SettingsDialog.SettingsDialogListener {

    private Button main_BTN_play;
    private Button main_BTN_topTen;
    private Button main_BTN_settings;

    private boolean sensors;
    private boolean sounds;

    MediaPlayer player;

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

    private View.OnClickListener settingsBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openSettingsDialog();
        }
    };

    private void openSettingsDialog() {
        SettingsDialog settingsDialog = new SettingsDialog();
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
    public void apply(boolean sensors, boolean sounds) {
        this.sensors = sensors;
        this.sounds = sounds;

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
}

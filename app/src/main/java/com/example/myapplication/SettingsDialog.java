package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsDialog extends AppCompatDialogFragment {

    private SwitchMaterial settings_SWITCH_sensors;
    private SwitchMaterial settings_SWITCH_sounds;
    private SwitchMaterial settings_SWITCH_vibrator;

    private SettingsDialogListener settingsDialogListener;

    private boolean sensors = false;
    private boolean sounds = false;
    private boolean vibrator = false;

    SettingsDialog(){}
    SettingsDialog(boolean sensors, boolean sounds, boolean vibrator) {
        this.sensors = sensors;
        this.sounds = sounds;
        this.vibrator = vibrator;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_settings_dialog, null);
        findViews(view);
        settings_SWITCH_sensors.setChecked(sensors);
        settings_SWITCH_sounds.setChecked(sounds);
        settings_SWITCH_vibrator.setChecked(vibrator);
        builder
                .setView(view)
                .setTitle("Settings")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settingsDialogListener.applySettings(
                                settings_SWITCH_sensors.isChecked(),
                                settings_SWITCH_sounds.isChecked(),
                                settings_SWITCH_vibrator.isChecked());
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            settingsDialogListener = (SettingsDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() +
                            "must implement SettingsDialogListener"
            );
        }
    }

    public interface SettingsDialogListener {
        void applySettings(boolean sensors, boolean sounds, boolean vibrator);
    }

    private void findViews(View view) {
        settings_SWITCH_sensors = view.findViewById(R.id.settings_SWITCH_sensors);
        settings_SWITCH_sounds = view.findViewById(R.id.settings_SWITCH_sounds);
        settings_SWITCH_vibrator = view.findViewById(R.id.settings_SWITCH_vibrator);
    }
}

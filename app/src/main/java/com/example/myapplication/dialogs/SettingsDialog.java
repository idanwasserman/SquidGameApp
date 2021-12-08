package com.example.myapplication.dialogs;

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

import com.example.myapplication.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsDialog extends AppCompatDialogFragment {

    private SwitchMaterial settings_SWITCH_sounds;
    private SwitchMaterial settings_SWITCH_vibrations;

    private SettingsDialogListener settingsDialogListener;

    private boolean sounds = false;
    private boolean vibrations = true;

    SettingsDialog(boolean sounds, boolean vibrator) {
        this.sounds = sounds;
        this.vibrations = vibrator;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_settings_dialog, null);
        findViews(view);

        settings_SWITCH_sounds.setChecked(sounds);
        settings_SWITCH_vibrations.setChecked(vibrations);

        builder
                .setView(view)
                .setTitle(R.string.settings)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settingsDialogListener.applySettings(
                                settings_SWITCH_sounds.isChecked(),
                                settings_SWITCH_vibrations.isChecked());
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
        void applySettings(boolean sounds, boolean vibrations);
    }

    private void findViews(View view) {
        settings_SWITCH_sounds = view.findViewById(R.id.settings_SWITCH_sounds);
        settings_SWITCH_vibrations = view.findViewById(R.id.settings_SWITCH_vibrations);
    }
}

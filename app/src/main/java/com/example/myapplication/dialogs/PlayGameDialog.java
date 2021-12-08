package com.example.myapplication.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;


public class PlayGameDialog extends AppCompatDialogFragment {

    private static final String DEFAULT_NICKNAME = "Player_456";
    private final String SENSORS = "Sensors";

    private String nickname = "";
    private boolean sensors = false;

    private TextInputEditText dialog_TIET_nickname;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private PlayGameDialogListener playGameDialogListener;

    private AppCompatActivity activity;


    PlayGameDialog(AppCompatActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_play_dialog, null);
        findViews(view);

        builder
                .setView(view)
                .setTitle(R.string.play)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .setPositiveButton(R.string.start_game, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getNickname();
                        getRadioGroupChoice(view);
                        playGameDialogListener.apply(nickname, sensors);
                    }
                });

        return builder.create();
    }

    private void getNickname() {
        nickname = dialog_TIET_nickname.getText().toString();
        if (nickname.length() == 0) {
            nickname = DEFAULT_NICKNAME;
        }
    }

    private void getRadioGroupChoice(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        if (radioId == -1) {
            Toast.makeText(
                    activity.getApplicationContext(),
                    "Default Mode: Buttons",
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            radioButton = view.findViewById(radioId);
            sensors = radioButton
                    .getText()
                    .toString()
                    .equals(SENSORS);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            playGameDialogListener = (PlayGameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() +
                            "must implement PlayGameDialogListener"
            );
        }
    }

    public interface PlayGameDialogListener {
        void apply(String nickname, boolean sensors);
    }

    private void findViews(View view) {
        dialog_TIET_nickname = view.findViewById(R.id.dialog_TIET_nickname);
        radioGroup = view.findViewById(R.id.radioGroup);
    }

}

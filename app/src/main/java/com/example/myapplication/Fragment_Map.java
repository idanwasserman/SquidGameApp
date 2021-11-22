package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.objects.Record;

import java.util.ArrayList;

public class Fragment_Map extends Fragment {

    private AppCompatActivity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void initViews() {

    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }


    private void findViews(View view) {


    }

}

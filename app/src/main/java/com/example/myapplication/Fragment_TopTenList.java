package com.example.myapplication;

import android.content.Context;
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

public class Fragment_TopTenList extends Fragment {

    private AppCompatActivity activity;

    private TextView[] list_TXT_arr;

    private CallBack_List callBack_list;

    private ArrayList<Record> topTen;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_ten_list, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void initViews() {

    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBack_list(CallBack_List callBack_list) {
        this.callBack_list = callBack_list;
    }

    private void findViews(View view) {

        list_TXT_arr = new TextView[] {
                view.findViewById(R.id.list_TXT_1),
                view.findViewById(R.id.list_TXT_2),
                view.findViewById(R.id.list_TXT_3),
                view.findViewById(R.id.list_TXT_4),
                view.findViewById(R.id.list_TXT_5),
                view.findViewById(R.id.list_TXT_6),
                view.findViewById(R.id.list_TXT_7),
                view.findViewById(R.id.list_TXT_8),
                view.findViewById(R.id.list_TXT_9),
                view.findViewById(R.id.list_TXT_10)
        };
    }
}

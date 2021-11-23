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

import com.example.myapplication.objects.MyDatabase;
import com.example.myapplication.objects.Record;
import com.example.myapplication.objects.SortRecordByScore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fragment_TopTenList extends Fragment {

    private AppCompatActivity activity;

    private TextView[] list_TXT_arr;

    private CallBack_List callBack_list;

    private List<Record> topTen;

    private final String MY_DB_NAME = "SQUID_GAME_DB";
    private final String defDbVal = "{\"records\":[]}";
    private final int TEN = 10;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_ten_list, container, false);

        findViews(view);
        fillTopTenList();
        showTopTenList();

        return view;
    }

    private void showTopTenList() {
        int listSize = topTen.size();
        for (int i = 0; i < listSize; i++) {
            list_TXT_arr[i].setText((i + 1) + ") " + (topTen.get(i).toString()));
        }

        // If there are less than 10 records --> fill empty lines
        for (int i = listSize; i < TEN; i++) {
            list_TXT_arr[i].setText((i +1 ) + ")\t-----");
        }
    }

    private void fillTopTenList() {
        // Fetch database
        String str_db = MySharedPreferences.getInstance().getString(MY_DB_NAME, defDbVal);
        MyDatabase my_db = new Gson().fromJson(str_db, MyDatabase.class);

        // Fetch records from database and sort them by score
        ArrayList<Record> records = my_db.getRecords();
        int arrSize = records.size();
        Collections.sort(records, new SortRecordByScore());

        // Get the top ten scores
        int from = 0;
        if (records.size() >= TEN) {
            from = arrSize - 10;
        }
        topTen = records.subList(from, arrSize);

        // Reverse list
        Collections.reverse(topTen);
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

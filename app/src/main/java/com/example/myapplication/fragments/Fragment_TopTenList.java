package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.interfaces.CallBack_List;
import com.example.myapplication.objects.MySharedPreferences;
import com.example.myapplication.R;
import com.example.myapplication.objects.MyDatabase;
import com.example.myapplication.objects.Record;
import com.example.myapplication.objects.SortRecordByScore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fragment_TopTenList extends Fragment {

    private AppCompatActivity activity;
    private Button[] list_TXT_arr;
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
        initViews();

        return view;
    }

    private void initViews() {
        int size = topTen.size();

        if (size > 0) {
            list_TXT_arr[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_list.setMapLocation(topTen.get(0).getLat(), topTen.get(0).getLng());
                }
            });
        }

        if (size > 1) {
            list_TXT_arr[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_list.setMapLocation(topTen.get(1).getLat(), topTen.get(1).getLng());
                }
            });
        }

        if (size > 2) {
            list_TXT_arr[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_list.setMapLocation(topTen.get(2).getLat(), topTen.get(2).getLng());
                }
            });
        }

        if (size > 3) {
            list_TXT_arr[3].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_list.setMapLocation(topTen.get(3).getLat(), topTen.get(3).getLng());
                }
            });
        }

        if (size > 4) {
            list_TXT_arr[4].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_list.setMapLocation(topTen.get(4).getLat(), topTen.get(4).getLng());
                }
            });
        }

        if (size > 5) {
            list_TXT_arr[5].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_list.setMapLocation(topTen.get(5).getLat(), topTen.get(5).getLng());
                }
            });
        }

        if (size > 6) {
            list_TXT_arr[6].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_list.setMapLocation(topTen.get(6).getLat(), topTen.get(6).getLng());
                }
            });
        }

        if (size > 7) {
            list_TXT_arr[7].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_list.setMapLocation(topTen.get(7).getLat(), topTen.get(7).getLng());
                }
            });
        }

        if (size > 8) {
            list_TXT_arr[8].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_list.setMapLocation(topTen.get(8).getLat(), topTen.get(8).getLng());
                }
            });
        }

        if (size > 9) {
            list_TXT_arr[9].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_list.setMapLocation(topTen.get(9).getLat(), topTen.get(9).getLng());
                }
            });
        }
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
        list_TXT_arr = new Button[] {
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

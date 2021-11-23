package com.example.myapplication.objects;

import java.util.ArrayList;

public class MyDatabase {

    private ArrayList<Record> records;

    public MyDatabase() {
        records = new ArrayList<>();
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public MyDatabase setRecords(ArrayList<Record> records) {
        this.records = records;
        return this;
    }

}

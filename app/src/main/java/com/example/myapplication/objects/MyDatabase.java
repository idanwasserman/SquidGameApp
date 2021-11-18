package com.example.myapplication.objects;

import java.util.AbstractList;
import java.util.ArrayList;

public class MyDatabase {

    private AbstractList<Record> records = new ArrayList<>();

    public MyDatabase() {
    }

    public AbstractList<Record> getRecords() {
        return records;
    }

    public MyDatabase setRecords(AbstractList<Record> records) {
        this.records = records;
        return this;
    }
}

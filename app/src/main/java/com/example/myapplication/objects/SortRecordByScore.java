package com.example.myapplication.objects;

import java.util.Comparator;

public class SortRecordByScore implements Comparator<Record> {

    @Override
    public int compare(Record o1, Record o2) {
        if (o1.getScore() != o2.getScore()) {
            return o1.getScore() - o2.getScore();
        } else {
            return o1.getDate().compareTo(o2.getDate());
        }
    }
}

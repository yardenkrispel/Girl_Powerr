package com.example.Girl_Power.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class RecordsList {
    private ArrayList<Record> records = new ArrayList<Record>();

    public RecordsList(){
    }

    public RecordsList setRecords(ArrayList<Record> records) {
        this.records = records;
        Collections.sort(records, Collections.reverseOrder());
        return this;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public ArrayList<Record> getTopRecords(int length){
        ArrayList<Record> topRecords = new ArrayList<Record>();
        for(int i = 0; i < length && i < records.size(); i++){
            topRecords.add(this.records.get(i));
        }
        Collections.sort(topRecords, Collections.reverseOrder());
        return topRecords;
    }

    public boolean add(Record record){
        boolean res = this.records.add(record);
        Collections.sort(this.records);
        return res;
    }
}

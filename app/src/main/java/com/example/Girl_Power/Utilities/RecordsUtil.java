package com.example.Girl_Power.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.Girl_Power.Model.Record;
import com.example.Girl_Power.Model.RecordsList;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RecordsUtil {

    private static volatile RecordsUtil instance = null;
    private static final String RECORDS_FILE = "RECORDS_FILE";
    private static final String KEY_RECORDS = "KEY_RECORDS";
    private RecordsList records;
    private SharedPreferences sharedPref;

    private RecordsUtil(Context context) {
        this.sharedPref = context.getSharedPreferences(RECORDS_FILE, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        synchronized (RecordsUtil.class) {
            if (instance == null) {
                instance = new RecordsUtil(context);
                instance.fetchFromDb();
            }
        }
    }

    public static RecordsUtil getInstance() {
        return instance;
    }

    private void fetchFromDb(){
        String res = getInstance().sharedPref.getString(KEY_RECORDS,"");
        if(res.equals(""))
            this.records = new RecordsList();
        else {
            this.records = new Gson().fromJson(res, RecordsList.class);
        }
    }

    private void saveToDB(){
        String json = new Gson().toJson(this.records);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_RECORDS, json);
        editor.apply();
    }

    public ArrayList<Record> getRecords(){
        return this.records.getRecords();
    }

    public ArrayList<Record> getTopRecords(int length){
        return this.records.getTopRecords(length);
    }

    public void addRecord(int score, double lat, double lon){
        Record record = new Record();
        record.setScore(score)
                .setLat(lat)
                .setLon(lon)
                .setDate(DateFormatter.getDatetime());
        this.records.add(record);
        saveToDB();
    }
}

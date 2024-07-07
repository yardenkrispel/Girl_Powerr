package com.example.Girl_Power;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.Girl_Power.Fragments.MapFragment;
import com.example.Girl_Power.Fragments.RecordsFragment;
import com.example.Girl_Power.Interfaces.Callback_recordClicked;

public class TopRecordsActivity extends AppCompatActivity {

    private RecordsFragment recordsFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_records);

        initViews();

        getSupportFragmentManager().beginTransaction().add(R.id.topRecords_LBL_records, recordsFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.topRecords_LBL_map, mapFragment).commit();
    }

    private void initViews() {
        mapFragment = new MapFragment(this);
        recordsFragment = new RecordsFragment(this);
        recordsFragment.setCallbackRecordClicked(new Callback_recordClicked() {
            @Override
            public void recordClicked(double lat, double lon) {
                mapFragment.find(lat,lon);
            }
        });
    }


}
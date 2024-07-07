package com.example.Girl_Power.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Girl_Power.Adapters.RecordAdapter;
import com.example.Girl_Power.Interfaces.Callback_recordClicked;
import com.example.Girl_Power.Model.Record;
import com.example.Girl_Power.R;
import com.example.Girl_Power.Utilities.RecordsUtil;

import java.util.ArrayList;

public class RecordsFragment extends Fragment {

    private RecyclerView fRecords_LST_recordsList;
    private Callback_recordClicked callbackRecordClicked;
    private AppCompatActivity context;
    private ArrayList<Record> records;

    public RecordsFragment(AppCompatActivity context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records, container, false);

        findViews(view);
        initViews(view);
        return view;
    }

    private void findViews(View view) {
        fRecords_LST_recordsList = view.findViewById(R.id.fRecords_LST_recordsList);
    }

    private void initViews(View view) {
        this.records = RecordsUtil.getInstance().getTopRecords(10);
        RecordAdapter recordAdapter = new RecordAdapter(this.context, this.records);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        fRecords_LST_recordsList.setLayoutManager(linearLayoutManager);
        fRecords_LST_recordsList.setAdapter(recordAdapter);

        recordAdapter.setRecordCallBack((index, record) -> {
                if (callbackRecordClicked != null)
                    callbackRecordClicked.recordClicked(record.getLat(), record.getLon());
        });
    }

    public void setCallbackRecordClicked(Callback_recordClicked callbackRecordClicked){
        this.callbackRecordClicked = callbackRecordClicked;
    }

}
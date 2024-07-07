package com.example.Girl_Power.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Girl_Power.Interfaces.RecordCallBack;
import com.example.Girl_Power.Model.Record;
import com.example.Girl_Power.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private Context context;
    private ArrayList<Record> records;
    private RecordCallBack recordCallBack;

    public RecordAdapter(Context context, ArrayList<Record> records) {
        this.context = context;
        this.records = records;
    }

    public void setRecordCallBack(RecordCallBack recordCallBack) {
        this.recordCallBack = recordCallBack;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_line, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = getRecord(position);
        holder.recordLine_LBL_line.setText("#" + (position + 1));
        holder.recordLine_LBL_date.setText("Date: " + record.getDate());
        holder.recordLine_LBL_score.setText("Score: " + record.getScore());
        holder.recordLine_LINE_line.setOnClickListener(v -> {
            if (recordCallBack != null){
                recordCallBack.recordClicked(position, record);
            }
        });
    }

    @Override
    public int getItemCount() {
        return records == null ? 0: records.size();
    }

    private Record getRecord(int position){
        return records.get(position);
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        private LinearLayoutCompat recordLine_LINE_line;
        private MaterialTextView recordLine_LBL_line;
        private MaterialTextView recordLine_LBL_score;
        private MaterialTextView recordLine_LBL_date;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            recordLine_LINE_line = itemView.findViewById(R.id.recordLine_LINE_line);
            recordLine_LBL_line = itemView.findViewById(R.id.recordLine_LBL_line);
            recordLine_LBL_score = itemView.findViewById(R.id.recordLine_LBL_score);
            recordLine_LBL_date = itemView.findViewById(R.id.recordLine_LBL_date);
        }
    }
}

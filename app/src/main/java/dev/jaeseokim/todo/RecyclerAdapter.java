package dev.jaeseokim.todo;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    //TO DO 데이터를 저장하기 위한 배열 선언
    private ArrayList<ToDoData> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lst_todo_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(ToDoData data) {
        listData.add(data);
    }

    public void clear() {
        listData.clear();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView time;
        private TextView id;
        private CheckBox checkBox;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.view_task_title);
            time = itemView.findViewById(R.id.view_task_time_stamp);
            id = itemView.findViewById(R.id.view_task_id);
            checkBox = itemView.findViewById(R.id.view_task_checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        MainActivity.dbHelper.upDateCheck(Integer.parseInt((String) id.getText()),1);
                    }else{
                        title.setPaintFlags(0);
                        MainActivity.dbHelper.upDateCheck(Integer.parseInt((String) id.getText()),0);
                    }
                }
            });
        }

        void onBind(ToDoData data) {
            title.setText(data.getTitle());
            time.setText(data.getTime());
            id.setText(String.valueOf(data.getId()));
            title.setTextColor(data.getTextColor());
            if (data.getCheck() == 1){
                checkBox.setChecked(true);
                title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else {
                checkBox.setChecked(false);
                title.setPaintFlags(0);
            }
        }
    }


}

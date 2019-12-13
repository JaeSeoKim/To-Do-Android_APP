package dev.jaeseokim.todo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ArchiveActivity extends AppCompatActivity {
    View archive_view;
    RecyclerAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        archive_view = findViewById(R.id.archive_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        setResult(RESULT_OK,intent);
        FloatingActionButton fab = findViewById(R.id.fab_archive_clear);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ArchiveActivity.this);
                builder.setTitle("All Clear!!!!!");
                builder.setMessage("Are You Sure?\n\n" +
                        "This action cannot be reversed.");
                builder.setPositiveButton("Clear!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.dbHelper.allClearArchive();
                        loadData();
                        Snackbar.make(archive_view,"All Cleared!!!",Snackbar.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No!", null);
                final AlertDialog dialog = builder.create();

                dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                    }
                });
                dialog.show();
            }
        });

        init();

        loadData();
    }

    private void loadData() {
        ArrayList<ToDoData> toDoData = MainActivity.dbHelper.getToDoData("1");
        adapter.clear();
        for (int i = 0; i < toDoData.size(); i++){
            ToDoData data = toDoData.get(i);
            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }

    private void init() {
        recyclerView = findViewById(R.id.rcvTodo);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            final int position = viewHolder.getAdapterPosition();
            adapter.notifyItemRemoved(position);
            View child = recyclerView.getChildAt(position);
            final TextView id = child.findViewById(R.id.view_task_id);
            MainActivity.dbHelper.upDateArchive(Integer.parseInt((String) id.getText()),0);
            Snackbar.make(archive_view,"Rollback!!",Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.dbHelper.upDateArchive(Integer.parseInt((String) id.getText()),1);
                    loadData();
                }
            }).show();
            loadData();
        }
    };

    //main에서는 내용을 보여줄때 사용 하였지만 여기서는 지울때 사용
    public void onClickedToDoView(View view){
        final TextView id = view.findViewById(R.id.view_task_id);
        AlertDialog.Builder builder = new AlertDialog.Builder(ArchiveActivity.this);
        builder.setTitle("Delete ToDo!!!!!");
        builder.setMessage("Are You Sure?\n\n" +
                "This action cannot be reversed.");
        builder.setPositiveButton("Clear!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.dbHelper.deleteToDo(id.getText().toString());
                loadData();
                Snackbar.make(archive_view,"Deleted!!!",Snackbar.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No!", null);
        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.show();
    }
}

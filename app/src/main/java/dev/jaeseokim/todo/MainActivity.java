package dev.jaeseokim.todo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static DbHelper dbHelper;
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    View root_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root_view = findViewById(R.id.root_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DbHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,NewToDoActivity.class);
                startActivityForResult(intent,1);
            }
        });

        init();

        loadData();
    }

    private void loadData() {;
        ArrayList<ToDoData> toDoData = dbHelper.getToDoData("0");
        adapter.clear();
        for (int i = 0; i < toDoData.size(); i++){
            ToDoData data = toDoData.get(i);
            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) // 액티비티가 정상적으로 종료되었을 경우
        {
            if(requestCode==1) // requestCode==1 로 호출한 경우에만 처리합니다.
            {
                String title = data.getStringExtra("title");
                String desc = data.getStringExtra("desc");
                int color = data.getIntExtra("textColor",1);
                dbHelper.insertToDo(title,desc,color);
            }
            else if (requestCode==2){
                String title = data.getStringExtra("title_edited");
                String desc = data.getStringExtra("desc_edited");
                int color = data.getIntExtra("textColor_edited",1);
                int id = data.getIntExtra("id",0);
                dbHelper.updateToDo(id,title,desc,color);
            }
            loadData();
        }
    }

    private void init(){
        recyclerView = findViewById(R.id.rcvTodo);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this,About.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_archive){
            Intent intent = new Intent(this,ArchiveActivity.class);
            startActivityForResult(intent,3);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickedToDoView(View view){
        Intent intent= new Intent(this,ViewToDoActivity.class);
        TextView id = view.findViewById(R.id.view_task_id);
        ToDoData data = dbHelper.getToDo(Integer.parseInt((String) id.getText()));
        intent.putExtra("title",data.getTitle());
        intent.putExtra("time",data.getTime());
        intent.putExtra("desc",data.getDesc());
        intent.putExtra("text_color",data.getTextColor());
        intent.putExtra("id",data.getId());
        startActivityForResult(intent,2);
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
            dbHelper.upDateArchive(Integer.parseInt((String) id.getText()),1);
            Snackbar.make(root_view,"Archived!!",Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbHelper.upDateArchive(Integer.parseInt((String) id.getText()),0);
                    loadData();
                }
            }).show();
            loadData();
        }
    };


}

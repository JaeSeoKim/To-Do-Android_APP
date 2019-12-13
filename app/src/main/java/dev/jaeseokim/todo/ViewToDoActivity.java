package dev.jaeseokim.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ViewToDoActivity extends AppCompatActivity {

    TextView title, time, desc;
    Intent this_intent;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_todo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this_intent = getIntent();

        title = findViewById(R.id.view_todo_title);
        time = findViewById(R.id.view_todo_time);
        desc = findViewById(R.id.view_todo_desc);

        title.setText(this_intent.getStringExtra("title"));
        title.setTextColor(this_intent.getIntExtra("text_color",0));
        time.setText(this_intent.getStringExtra("time"));
        desc.setText(this_intent.getStringExtra("desc"));

        id = this_intent.getIntExtra("id",0);


        FloatingActionButton fab = findViewById(R.id.fab_edit_todo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ViewToDoActivity.this,EditToDoActivity.class);
                intent.putExtra("id",id);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) // 액티비티가 정상적으로 종료되었을 경우
        {
            if(requestCode==1) // requestCode==1 로 호출한 경우에만 처리합니다.
            {
                String title = data.getStringExtra("title_edited");
                String desc = data.getStringExtra("desc_edited");
                int color = data.getIntExtra("textColor_edited",1);
                this_intent.putExtra("title_edited",title);
                this_intent.putExtra("desc_edited",desc);
                this_intent.putExtra("textColor_edited",color);
                this_intent.putExtra("id_edited",id);
                setResult(RESULT_OK, this_intent);
                finish();
            }
        }
    }



}

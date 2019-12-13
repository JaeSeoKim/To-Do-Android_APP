package dev.jaeseokim.todo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import static dev.jaeseokim.todo.R.id.highlight_black;
import static dev.jaeseokim.todo.R.id.highlight_blue;
import static dev.jaeseokim.todo.R.id.highlight_green;
import static dev.jaeseokim.todo.R.id.highlight_purple;
import static dev.jaeseokim.todo.R.id.highlight_red;
import static dev.jaeseokim.todo.R.id.highlight_white_blue;
import static dev.jaeseokim.todo.R.id.highlight_yellow;

public class NewToDoActivity extends AppCompatActivity {

    EditText title, desc;
    RadioGroup highlight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intent intent = getIntent();
        FloatingActionButton fab = findViewById(R.id.fab_save_todo);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                title = (EditText) findViewById(R.id.edit_todo_title);
                desc = (EditText) findViewById(R.id.edit_todo_desc);
                highlight = (RadioGroup) findViewById(R.id.edit_todo_highlight);
                if(title.getText().toString().trim().equals("")){
                    Snackbar.make(view, "title is required", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    int color;
                    switch (highlight.getCheckedRadioButtonId()){
                        case highlight_black:
                            color = ContextCompat.getColor(getApplicationContext(), R.color.textHighlight_BLACK);;
                            break;
                        case highlight_red:
                            color = ContextCompat.getColor(getApplicationContext(), R.color.textHighlight_RED);
                            break;
                        case highlight_yellow:
                            color = ContextCompat.getColor(getApplicationContext(), R.color.textHighlight_YELLOW);
                            break;
                        case highlight_green:
                            color = ContextCompat.getColor(getApplicationContext(), R.color.textHighlight_GREEN);
                            break;
                        case highlight_white_blue:
                            color = ContextCompat.getColor(getApplicationContext(), R.color.textHighlight_WHITEBLUE);
                            break;
                        case highlight_blue:
                            color = ContextCompat.getColor(getApplicationContext(), R.color.textHighlight_BLUE);
                            break;
                        case highlight_purple:
                            color = ContextCompat.getColor(getApplicationContext(), R.color.textHighlight_PUPLUE);
                            break;
                        default:
                            color = ContextCompat.getColor(getApplicationContext(), R.color.textHighlight_BLACK);
                            break;
                    }

                    if(intent != null){
                        intent.putExtra("title",title.getText().toString());
                        intent.putExtra("desc",desc.getText().toString());
                        intent.putExtra("textColor", color);
                    }
                    setResult(RESULT_OK,intent);
                    finish();
                }
                }
        });

    }


}

package dev.jaeseokim.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DevJS";
    private static final int DB_VER = 1;
    private static final String DB_TABLE_TODO = "ToDo";
    private static final String DB_COLUMN_ID = "ToDoId";
    private static final String DB_COLUMN_TITLE = "ToDoTitle";
    private static final String DB_COLUMN_DESCRIPTION = "ToDoDescription";
    private static final String DB_COLUMN_TIME_STAMP = "ToDoTimeStamp";
    private static final String DB_COLUMN_HIGHLIGHT = "ToDoHighLight";
    private static final String DB_COLUMN_CHECKED = "ToDoChecked";
    private static final String DB_COLUMN_ARCHIVE = "ToDoArchive";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format(
                "CREATE TABLE %s" +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT DEFAULT 'No Description!'," +
                        "%s DATETIME DEFAULT (datetime('now', 'localtime'))," +
                        "%s INTEGER DEFAULT 1," +
                        "%s INTEGER DEFAULT 0," +
                        "%s INTEGER DEFAULT 0 );",
                DB_TABLE_TODO,
                DB_COLUMN_ID,
                DB_COLUMN_TITLE,
                DB_COLUMN_DESCRIPTION,
                DB_COLUMN_TIME_STAMP,
                DB_COLUMN_HIGHLIGHT,
                DB_COLUMN_CHECKED,
                DB_COLUMN_ARCHIVE);
        //ID를 Primary Key 로 등록을 하고 Title, time, desc, color, check, archive 에 대해 저장공간 선언
        try{
            db.execSQL(query);
            Log.d("SQL CREATE","success");
        }catch (Exception e){
            Log.d("SQL CREATE","fail");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s",DB_TABLE_TODO);
        db.execSQL(query);
        onCreate(db);
        //DB에 대해 버전 변경점이 있을때 새로 생성
    }

    public void insertToDo(String title, String desc, int color){
        //할일(task)에 대해 인자로 넘겨 받음
        SQLiteDatabase db= this.getWritableDatabase();
        //SQLite 객채 생성 (쓰기 권한)
        ContentValues values = new ContentValues();
        //ContentValues 객체 생성
        values.put(DB_COLUMN_TITLE,title);
        values.put(DB_COLUMN_DESCRIPTION,desc);
        values.put(DB_COLUMN_HIGHLIGHT,color);
        //데이터 전달
        db.insertWithOnConflict(DB_TABLE_TODO,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        //insert
        Log.e("DB Insert", title+":"+desc+":"+color);
        //처리가 완료되면 Log 생성
        db.close();
        //db 객체 닫음
    }


    public ToDoData getToDo(int id){
        ToDoData data = new ToDoData();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =db.query(DB_TABLE_TODO,
                new String[] {DB_COLUMN_TITLE,DB_COLUMN_TIME_STAMP,DB_COLUMN_DESCRIPTION,DB_COLUMN_HIGHLIGHT,DB_COLUMN_ID,DB_COLUMN_CHECKED},
                DB_COLUMN_ID + "=?",
                new String[] {String.valueOf(id)},null,null,null);
        if(cursor != null)
            cursor.moveToFirst();
        data.setTitle(cursor.getString(0));
        data.setTime(cursor.getString(1));
        data.setDesc(cursor.getString(2));
        data.setTextColor(cursor.getInt(3));
        data.setId(cursor.getInt(4));
        data.setCheck(cursor.getInt(5));
        db.close();
        return data;
    }

    public ArrayList<ToDoData> getToDoData(String archive){
        ArrayList<ToDoData> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //SQLite 객채 생성 (읽기 권한)
        Cursor cursor = db.query(DB_TABLE_TODO,new String[]{DB_COLUMN_TITLE,DB_COLUMN_TIME_STAMP,DB_COLUMN_DESCRIPTION,DB_COLUMN_HIGHLIGHT,DB_COLUMN_ID,DB_COLUMN_CHECKED},
                DB_COLUMN_ARCHIVE + "= ?",new String[] {archive},
                null,null,null);
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_COLUMN_ID);
            ToDoData todo = new ToDoData();
            todo.setTitle(cursor.getString(0));
            todo.setTime(cursor.getString(1));
            todo.setDesc(cursor.getString(2));
            todo.setTextColor(cursor.getInt(3));
            todo.setId(cursor.getInt(4));
            todo.setCheck(cursor.getInt(5));
            data.add(todo);
        }
        cursor.close();
        //cursor 객체 닫음
        db.close();
        //db 객체 닫음
        return data;
    }

    public void upDateCheck(int id,int flag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_CHECKED,flag);
        db.update(DB_TABLE_TODO,values,DB_COLUMN_ID+"=?",new String[]{String.valueOf(id)});
        Log.d("Checked",String.valueOf(id));
    }

    public void upDateArchive(int id,int flag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_ARCHIVE,flag);
        db.update(DB_TABLE_TODO,values,DB_COLUMN_ID+"=?",new String[]{String.valueOf(id)});
        Log.d("Archive",String.valueOf(id));
    }

    public void deleteToDo(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE_TODO,DB_COLUMN_ID + " = ?",new String[]{id});
        db.close();
        Log.d("deleteToDo",id);
    }

    public void updateToDo(int id, String title, String desc, int color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_TITLE,title);
        values.put(DB_COLUMN_DESCRIPTION,desc);
        values.put(DB_COLUMN_HIGHLIGHT,color);
        db.update(DB_TABLE_TODO,values,DB_COLUMN_ID+"=?",new String[]{String.valueOf(id)});
        Log.d("updateToDo",String.valueOf(id));
    }

    public void allClearArchive(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE_TODO,DB_COLUMN_ARCHIVE + " = ?",new String[]{"1"});
        db.close();
        Log.d("allClearArchive","clear");
    }
}

package com.kankanla.e560.m180907_morse;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.util.Log;

public class Morse_SQL {
    private final String TAG = "###Morse_SQL###";
    private Context context;
    private final int DB_VERSION = 1;
    private final String DB_NAME = "MSQL.db";
    private SQL_DB sql_db;


    public Morse_SQL(Context context) {
        Log.d(TAG, "add_item");
        Log.d(getClass().getName(), "333");
        this.context = context;
        sql_db = new SQL_DB(context, DB_NAME, null, DB_VERSION);
    }


    public void add_item(Editable title, Editable CODE) {
        Log.d(TAG, "add_item");
        SQLiteDatabase db = sql_db.getWritableDatabase();
        String stime = String.valueOf(System.currentTimeMillis());
        String cmd = "insert into main (title,code,create_time)values(?,?,?)";
        db.execSQL(cmd, new String[]{String.valueOf(title), String.valueOf(CODE), stime});
        db.close();
    }


    public Cursor list_item() {
        Log.d(TAG, "list_item");
        SQLiteDatabase db = sql_db.getReadableDatabase();
        String cmd = "select * from main";
        Cursor cursor = db.rawQuery(cmd, null);
        return cursor;
    }


    public class SQL_DB extends SQLiteOpenHelper {

        public SQL_DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "onCreate(SQLiteDatabase db)");
            String create_db = "create table main(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title text not null," +
                    "code text not null," +
                    "create_time Integer not null," +
                    "acccond integer default 0," +
                    "active text default 1 )";
            db.execSQL(create_db);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


}

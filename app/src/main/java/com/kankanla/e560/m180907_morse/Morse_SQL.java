package com.kankanla.e560.m180907_morse;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Morse_SQL {
    private final String TAG = "###Morse_SQL###";
    private Context context;
    private final int DB_VERSION = 1;
    private final String DB_NAME = "MSQL.db";
    private SQL_DB sql_db;

    public Morse_SQL(Context context) {
        Log.d(TAG, "add_item");
        this.context = context;
        sql_db = new SQL_DB(context, DB_NAME, null, DB_VERSION);
    }

    public int del_temp(int id) {
        Log.d(TAG, "del_temp");
        if (find_item_id(id) == null) {
            return 0;
        }
        SQLiteDatabase db = sql_db.getWritableDatabase();
        String del_item = "delete from main where id = ?";
        Cursor cursor = db.rawQuery(del_item, new String[]{String.valueOf(id)});
        int temp = cursor.getCount();
        cursor.close();
        db.close();
        return temp;
    }

    public String find_item_id(int id) {
        Log.d(TAG, "find_item_id");
        SQLiteDatabase db = sql_db.getReadableDatabase();
        String find_item_db = "select id from main where id = ?";
        Cursor cursor = db.rawQuery(find_item_db, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        String item_id = cursor.getString(cursor.getColumnIndex("id"));
        cursor.close();
        db.close();
        if (item_id == null) {
            return null;
        } else {
            return item_id;
        }
    }

    public void add_item(Editable title, Editable CODE, int id) {
        Log.d(TAG, "add_item");
        SQLiteDatabase db = sql_db.getWritableDatabase();
        String stime = String.valueOf(System.currentTimeMillis());
        String temp;
        if (id == 0) {
            temp = "insert into main (title,code,create_time)values(?,?,?)";
            db.execSQL(temp, new String[]{String.valueOf(title), String.valueOf(CODE), stime});
        } else {
            temp = "update main set title = ? ,code = ? where id = ?";
            db.execSQL(temp, new String[]{String.valueOf(title), String.valueOf(CODE), String.valueOf(id)});
        }
        db.close();
    }

    public String get_item_code(int id) {
        Log.d(TAG, "get_item");
        SQLiteDatabase db = sql_db.getReadableDatabase();
        String cmd = "select code from main where id = ?";
        Cursor cursor = db.rawQuery(cmd, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        String code = cursor.getString(cursor.getColumnIndex("code"));
        db.close();
        cursor.close();
        return code;
    }

    public String[] get_item(int id) {
        Log.d(TAG, "get_item");
        SQLiteDatabase db = sql_db.getReadableDatabase();
        String cmd = "select title,code from main where id = ?";
        Cursor cursor = db.rawQuery(cmd, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        String[] temp = new String[]{
                cursor.getString(cursor.getColumnIndex("code")),
                cursor.getString(cursor.getColumnIndex("title"))
        };
        db.close();
        cursor.close();
        return temp;
    }

    public void add_QRA() {
        HashMap<String, String> temp = new HashMap<>();
        temp.put("MyCQCQ", "CQCQCQ DE JAPAN");
        temp.put("QRA", "QRA");
        temp.put("QRL", "QRL");
        temp.put("QRB", "QRB");
        temp.put("QRK5", "QRK5");
        temp.put("QRM5", "QRM5");

        SQLiteDatabase db = sql_db.getWritableDatabase();
        String cmd = "select id from main";
        Cursor cursor = db.rawQuery(cmd, new String[]{});
        String cmd2 = "insert into main (title,code,create_time) values(?,?,?)";
        if (cursor.getCount() == 0) {
            for (Map.Entry<String, String> key : temp.entrySet()) {
                String stime = String.valueOf(System.currentTimeMillis());
                db.execSQL(cmd2, new String[]{key.getKey(), key.getValue(), stime});
            }
        }
        db.close();
    }


    public Cursor list_item() {
        Log.d(TAG, "list_item");
        SQLiteDatabase db = sql_db.getReadableDatabase();
        String cmd = "select * from main order by id desc";
        Cursor cursor = db.rawQuery(cmd, null);
        if (cursor.getCount() == 0) {
            add_QRA();
        }
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

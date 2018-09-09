package com.kankanla.e560.m180907_morse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Morse_SQL {
    private Context context;
    private final String TAG = "###Morse_SQL###";
    private final int DB_VERSION = 1;
    private final String DB_NAME = "MSQL.db";
    private SQL_DB sql_db;


    public Morse_SQL(Context context) {
        this.context = context;
        sql_db = new SQL_DB(context, DB_NAME, null, DB_VERSION);
    }


    public class SQL_DB extends SQLiteOpenHelper {

        public SQL_DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
                Log.d(TAG,"onCreate(SQLiteDatabase db)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


}

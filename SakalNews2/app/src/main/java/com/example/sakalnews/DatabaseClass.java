package com.example.sakalnews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ankit on 9/25/17.
 */

public class DatabaseClass extends SQLiteOpenHelper {

    private static final String CREATE_NEWS= "CREATE TABLE NEWS_TABLE (NEWS_NAME TEXT,NEWS_IMAGE  BLOB)";

    public DatabaseClass(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

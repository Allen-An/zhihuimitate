package com.example.angelshao.zhihuimitate.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WebCacheDbHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "webCache.db";

    public WebCacheDbHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists Cache (id INTEGER primary key autoincrement,newsId INTEGER unique,json text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
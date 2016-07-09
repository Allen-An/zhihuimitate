package com.example.angelshao.zhihuimitate.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David-Ann on 2016/7/8 0008.
 */
public class CacheDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "cache.db";
    public static final String DB_CREATE = "create table CacheList (id INTEGER primary key " +
            "autoincrement,date INTEGER unique,json text)";
    public static final String DB_DROPTABLE = "drop table CacheList";

    public CacheDbHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DB_DROPTABLE);
        db.execSQL(DB_CREATE);
    }
}

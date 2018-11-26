package com.example.l3oom.walkout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "WalkOut";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Records";
    public static final String COL_STEP_COUNT = "step_count";
    public static final String COL_DISTANCE = "distance";
    public static final String COL_CALORIES = "calories";
    public static final String COL_TIME = "time";

    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_STEP_COUNT + " TEXT, " + COL_DISTANCE + " TEXT, "
                + COL_CALORIES + " TEXT ," + COL_TIME + " TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

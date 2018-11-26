package com.example.l3oom.walkout;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    SQLiteDatabase mDb;
    MyDbHelper mHelper;
    Cursor mCursor;

    public double steps = 0;
    public double distance = 0;
    public double cal = 0;
    public double min = 0;
    public double sec = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if (getIntent().getStringExtra("time") != null && getIntent().getStringExtra("cal") != null
                && getIntent().getStringExtra("distance") != null && getIntent().getStringExtra("stepCount") != null) {
            final AlertDialog.Builder viewDialog = new AlertDialog.Builder(this);
            viewDialog.setMessage("Step : " + getIntent().getStringExtra("stepCount") + "\nTime : " + getIntent().getStringExtra("time") + "\n" + "Calories : " + getIntent().getStringExtra("cal") + "\nDistance : " + getIntent().getStringExtra("distance"));
            viewDialog.show();
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.group_item1:
                        Intent intent = new Intent(MainActivity2.this,
                                MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.group_item2:
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.group_item2);

        ListView listView = (ListView) findViewById(R.id.listview);

        mHelper = new MyDbHelper(this);
        mDb = mHelper.getWritableDatabase();
        mCursor = mDb.rawQuery("SELECT " + MyDbHelper.COL_STEP_COUNT + ", " + MyDbHelper.COL_DISTANCE
                + ", " + MyDbHelper.COL_CALORIES + ", " + MyDbHelper.COL_TIME + " FROM " + MyDbHelper.TABLE_NAME, null);

        final ArrayList<String> dirArray = new ArrayList<String>();
        mCursor.moveToFirst();

        while (!mCursor.isAfterLast()) {
            steps += Double.valueOf(mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_STEP_COUNT)));
            distance += Double.valueOf(mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_DISTANCE)));
            cal += Double.valueOf(mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_CALORIES)));
            SimpleDateFormat format = new SimpleDateFormat("mm:ss:SSS");
            try {
                Date date = format.parse(mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_TIME)));
                min += date.getMinutes();
                sec += date.getSeconds();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dirArray.add("step : " + mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_STEP_COUNT)) + "\t\t"
                    + "distance : " + mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_DISTANCE)) + " km\t\t\n"
                    + "calories : " + mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_CALORIES)) + "\t\t\n"
                    + "time : " + mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_TIME)));
            mCursor.moveToNext();
        }

        ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dirArray);
        listView.setAdapter(adapterDir);

        TextView textView = (TextView) findViewById(R.id.total);
        textView.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_info_details, 0, 0, 0);
        textView.setText("\t Total Steps : " + steps + " \n\t Total Distance : " + String.format("%.3f",distance) + " km\n\t Total calories : " + String.format("%.3f",cal)+" KCal");

        final AlertDialog.Builder viewDetail = new AlertDialog.Builder(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView,
                                    int position, long mylng) {
                String information = dirArray.get(position);
                viewDetail.setIcon(android.R.drawable.btn_star_big_on);
                viewDetail.setTitle("รายละเอียด");
                viewDetail.setMessage(information);
                viewDetail.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
                viewDetail.show();
            }
        });
    }

    public void onPause() {
        super.onPause();
        mHelper.close();
        mDb.close();
    }
}

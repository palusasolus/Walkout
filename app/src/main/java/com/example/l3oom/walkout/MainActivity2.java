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

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    MyDbHelper mHelper;
    Cursor mCursor;
    SQLiteDatabase mDb;

    public double steps = 0;
    public double distance = 0;
    public double cal = 0;
    public ArrayList<Integer> id = new ArrayList<>();

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

        final ListView listView = (ListView) findViewById(R.id.listview);

        mHelper = new MyDbHelper(this);
        mDb = mHelper.getWritableDatabase();
        mCursor = mDb.rawQuery("SELECT " + MyDbHelper.COL_ID + "," + MyDbHelper.COL_STEP_COUNT + ", " + MyDbHelper.COL_DISTANCE
                + ", " + MyDbHelper.COL_CALORIES + ", " + MyDbHelper.COL_TIME + " FROM " + MyDbHelper.TABLE_NAME, null);

        final ArrayList<String> dirArray = new ArrayList<String>();
        final ArrayList<String> dialogshow = new ArrayList<String>();
        mCursor.moveToFirst();

        while (!mCursor.isAfterLast()) {
            steps += Double.valueOf(mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_STEP_COUNT)));
            distance += Double.valueOf(mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_DISTANCE)));
            cal += Double.valueOf(mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_CALORIES)));
            id.add(Integer.valueOf(mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_ID))));
            dirArray.add("step : " + mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_STEP_COUNT)) + "\n"
                    + "time : " + mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_TIME)));
            dialogshow.add("step : " + mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_STEP_COUNT)) + "\n"
                    + "distance : " + mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_DISTANCE)) + " km\n"
                    + "calories : " + mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_CALORIES)) + "\n"
                    + "time : " + mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_TIME)));
            mCursor.moveToNext();
        }

        final ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dirArray);
        listView.setAdapter(adapterDir);

        final TextView textView = (TextView) findViewById(R.id.total);
        final TextView history = (TextView) findViewById(R.id.history);
        textView.setText("Total");
        history.setText("History");
        final TextView listTotal = (TextView) findViewById(R.id.listTotal);
        listTotal.setText("\t Steps : " + steps + " \n\t Distance : " + String.format("%.3f", distance) + " km\n\t calories : " + String.format("%.3f", cal) + " KCal");

        final AlertDialog.Builder viewDetail = new AlertDialog.Builder(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> myAdapter, View myView,
                                    final int position, long mylng) {
                String information = dialogshow.get(position);
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
                viewDetail.setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mDb.delete(MyDbHelper.TABLE_NAME, MyDbHelper.COL_ID + " = " + id.get(position), null);
                                resetData();
                                mCursor = mDb.rawQuery("SELECT " + MyDbHelper.COL_ID + "," + MyDbHelper.COL_STEP_COUNT + ", " + MyDbHelper.COL_DISTANCE
                                        + ", " + MyDbHelper.COL_CALORIES + ", " + MyDbHelper.COL_TIME + " FROM " + MyDbHelper.TABLE_NAME, null);

                                mCursor.moveToFirst();

                                while (!mCursor.isAfterLast()) {
                                    steps += Double.valueOf(mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_STEP_COUNT)));
                                    distance += Double.valueOf(mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_DISTANCE)));
                                    cal += Double.valueOf(mCursor.getString(mCursor.getColumnIndex(MyDbHelper.COL_CALORIES)));
                                    mCursor.moveToNext();
                                }
                                TextView textView = (TextView) findViewById(R.id.listTotal);
                                textView.setText("\t Steps : " + steps + " \n\t Distance : " + String.format("%.3f", distance) + " km\n\t calories : " + String.format("%.3f", cal) + " KCal");
                                textView.invalidate();
                                dialogshow.remove(position);
                                dirArray.remove(position);
                                adapterDir.notifyDataSetChanged();

                            }
                        });
                viewDetail.show();
            }
        });
    }

    public void resetData() {
        steps = 0;
        distance = 0;
        cal = 0;
    }

    public void onPause() {
        super.onPause();
        mHelper.close();
        mDb.close();
    }
}

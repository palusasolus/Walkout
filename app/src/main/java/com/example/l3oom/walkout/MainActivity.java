package com.example.l3oom.walkout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener {

    //StepDetector
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps,distance;
    private double kcal;
    private TextView stepCounted_value;
    private TextView distance_value;
    private TextView kcal_value;
    private static double METRIC_WALKING_FACTOR = 0.708;
    private static int STEP_LENGTH = 20;
    private static double AVERAGE_WEIGHT = 60;
    private double kilo,meters;




    public static final String SERVICECMD = "com.android.music.musicservicecommand";
    public static final String CMDNAME = "command";
    public static final String CMDTOGGLEPAUSE = "togglepause";
    public static final String CMDSTOP = "stop";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDNEXT = "next";
    AudioManager mAudioManager;

    public Long millSeconds = 0L, startTime = 0L, updateTime = 0L, timeSwap = 0L;
    Handler handlerTime = new Handler();
    Runnable runnableTime = new Runnable() {
        @Override
        public void run() {
            millSeconds = SystemClock.uptimeMillis() - startTime;
            updateTime = millSeconds + timeSwap;
            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            secs %= 60;
            int milli = (int) (updateTime % 1000);
            final TextView time = (TextView) findViewById(R.id.time);
            time.setText("" + String.format("%02d", mins) + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milli));
            handlerTime.postDelayed(this, 0);
        }
    };

    SQLiteDatabase mDb;
    MyDbHelper mHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        mAudioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);


    //Step Detector
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        stepCounted_value = (TextView) findViewById(R.id.stepCounted_value);
        distance_value = (TextView) findViewById(R.id.distance_value);
        kcal_value = (TextView) findViewById(R.id.kcal_value);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.group_item1:
                        // do this event
                        return true;
                    case R.id.group_item2:
                        Intent intent = new Intent(MainActivity.this,
                                MainActivity2.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.group_item1);

        final ImageButton playBtn = (ImageButton) findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAudioManager.isMusicActive()) {

                    long eventtime = SystemClock.uptimeMillis();

                    Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
                    KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
                    upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
                    sendOrderedBroadcast(upIntent, null);


//                    Intent i = new Intent("com.android.music.musicservicecommand");
//                    i.putExtra("command", "pause");
//                    sendBroadcast(i);
                    playBtn.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    long eventtime = SystemClock.uptimeMillis();

                    Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
                    KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
                    downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
                    sendOrderedBroadcast(downIntent, null);

//                    Intent i = new Intent("com.android.music.musicservicecommand");
//                    i.putExtra("command", "play");
//                    sendBroadcast(i);
                    playBtn.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        ImageButton nextBtn = (ImageButton) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAudioManager == null) {
                    mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
                }
                long eventtime = SystemClock.uptimeMillis();
                Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
                KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN,   KeyEvent.KEYCODE_MEDIA_NEXT, 0);
                downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
                sendOrderedBroadcast(downIntent, null);
//
//                Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
//                KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN,   KeyEvent.KEYCODE_MEDIA_NEXT, 0);
//                downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
//                sendOrderedBroadcast(downIntent, null);
//                Intent i = new Intent("com.android.music.musicservicecommand");
//                i.putExtra("command", "next");
//                sendBroadcast(i);
            }
        });

        final ImageButton previousBtn = (ImageButton) findViewById(R.id.previousBtn);
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAudioManager == null) {
                    mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
                }
                long eventtime = SystemClock.uptimeMillis();
                Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
                KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
                downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
                sendOrderedBroadcast(downIntent, null);
//
//                Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
//                KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
//                downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
//                sendOrderedBroadcast(downIntent, null);

//                Intent i = new Intent("com.android.music.musicservicecommand");
//                i.putExtra("command", "previous");
//                sendBroadcast(i);
            }
        });

        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.playbackcomplete");
        iF.addAction("com.android.music.queuechanged");

        registerReceiver(mReceiver, iF);






        final Button walkBtn = (Button) findViewById(R.id.WalkBtn);
        walkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (String.valueOf(walkBtn.getText())) {
                    case "Start Walking":
                        final ProgressBar progressBar1 = (ProgressBar)findViewById(R.id.progressBar);
                        progressBar1.setVisibility(View.VISIBLE);
                        walkBtn.setText("Stop Walking");
                        startTime = SystemClock.uptimeMillis();
                        handlerTime.postDelayed(runnableTime, 0);
                        numSteps = 0;
                        kcal = 0;
                        kilo=0l;
                        meters=0;
                        sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                        break;
                    case "Stop Walking":

//                        Log.i("Stop",""+time.getText());

                        // insert Db
                        // change to records
                        handlerTime.removeCallbacks(runnableTime);
                        final TextView time = (TextView) findViewById(R.id.time);
                        String timeDuration = String.valueOf(time.getText());
                        mHelper = new MyDbHelper(MainActivity.this);
                        mDb = mHelper.getWritableDatabase();
                        mDb.execSQL("INSERT INTO " + MyDbHelper.TABLE_NAME + " (" + MyDbHelper.COL_STEP_COUNT + ", " + MyDbHelper.COL_DURATION
                                + ", " + MyDbHelper.COL_DISTANCE + ") VALUES ('20', '" + timeDuration + "' , '3');");
                        Intent intent = new Intent(MainActivity.this,
                                MainActivity2.class);
                        intent.putExtra("pass", timeDuration);
                        startActivity(intent);
                        sensorManager.unregisterListener(MainActivity.this);
                        break;
                }
            }
        });
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @SuppressLint("LongLogTag")
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView nowPlayingTxt = (TextView) findViewById(R.id.now_playing_text);
            String action = intent.getAction();
            String cmd = intent.getStringExtra("command");
            Log.d("mIntentReceiver.onReceive ", action + " / " + cmd);
            String artist = intent.getStringExtra("artist");
            String album = intent.getStringExtra("album");
            String track = intent.getStringExtra("track");
            Log.d("Music", artist + ":" + album + ":" + track);
            nowPlayingTxt.setText(track + ": " + album + ": " + artist);

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        stepCounted_value.setText(numSteps + "");
    }

    @Override
    public void calDistance(){
        meters = ((numSteps * STEP_LENGTH)%10000)*0.00001;
        kilo = (numSteps * STEP_LENGTH)/10000;
        kilo+= (meters);

        distance_value.setText(String.format("%.3f",kilo));
    }
    @Override
    public void calKcal(){
        //     (mBodyWeight * (mIsRunning ? METRIC_RUNNING_FACTOR : METRIC_WALKING_FACTOR))
//            // Distance:
//            * mStepLength // centimeters
//                / 100000.0; // centimeters/kilometera
        kcal = ((AVERAGE_WEIGHT * METRIC_WALKING_FACTOR) * STEP_LENGTH /100000.0) * numSteps;
        kcal_value.setText(String.format("%.3f",kcal));
    }
}




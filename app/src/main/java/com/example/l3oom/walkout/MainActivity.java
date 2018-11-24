package com.example.l3oom.walkout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    public static final String SERVICECMD = "com.android.music.musicservicecommand";
    public static final String CMDNAME = "command";
    public static final String CMDTOGGLEPAUSE = "togglepause";
    public static final String CMDSTOP = "stop";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDNEXT = "next";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.bgBottomNavigation));
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.bgBottomNavigation));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.group_item1:
                        // do this event
                        return true;
                    case R.id.group_item2:
                        // do this event
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

                    Intent i = new Intent("com.android.music.musicservicecommand");

                    i.putExtra("command", "pause");
                    sendBroadcast(i);
                }
                else{
                    Intent i = new Intent("com.android.music.musicservicecommand");
                    i.putExtra("command", "play");
                    sendBroadcast(i);
//                    playBtn.setImageResource(R.drawable.ic);
                }
            }
        });

        ImageButton nextBtn = (ImageButton) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("com.android.music.musicservicecommand");
                i.putExtra("command", "next");
                sendBroadcast(i);
            }
        });

        ImageButton previousBtn = (ImageButton) findViewById(R.id.previousBtn);
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("com.android.music.musicservicecommand");
                i.putExtra("command", "previous");
                sendBroadcast(i);
            }
        });


//        long eventtime = SystemClock.uptimeMillis();

//        Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
//        KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
//        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
//        sendOrderedBroadcast(downIntent, null);
//        Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
//        KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
//        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
//        sendOrderedBroadcast(upIntent, null);
//
//        /*NEXT*/
//        Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
//        KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN,   KeyEvent.KEYCODE_MEDIA_NEXT, 0);
//        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
//        sendOrderedBroadcast(downIntent, null);
//
//        /*PREVIOUS*/
//        Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
//        KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
//        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
//        sendOrderedBroadcast(downIntent, null);

        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.playbackcomplete");
        iF.addAction("com.android.music.queuechanged");

        registerReceiver(mReceiver, iF);

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @SuppressLint("LongLogTag")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            TextView nowPlayingTxt = (TextView) findViewById(R.id.now_playing_text);
            String action = intent.getAction();
            String cmd = intent.getStringExtra("command");
            Log.d("mIntentReceiver.onReceive ", action + " / " + cmd);
            String artist = intent.getStringExtra("artist");
            String album = intent.getStringExtra("album");
            String track = intent.getStringExtra("track");
            Log.d("Music",artist+":"+album+":"+track);
            nowPlayingTxt.setText(track + " "+album +" "+artist);

        }
    };


}




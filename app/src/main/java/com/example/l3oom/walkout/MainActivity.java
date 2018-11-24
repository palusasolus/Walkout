package com.example.l3oom.walkout;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
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







        ImageButton playBtn = (ImageButton) findViewById(R.id.playBtn);
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
    }



}
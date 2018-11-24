package com.example.l3oom.walkout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.bgBottomNavigation));
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.bgBottomNavigation));
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
    }
}

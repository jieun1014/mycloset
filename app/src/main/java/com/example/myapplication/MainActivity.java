package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.BoardFragments.BoardFragment;
import com.example.myapplication.BoardFragments.BoardReadFragment;
import com.example.myapplication.BoardFragments.BoardWriteFragment;
import com.example.myapplication.ClosetFragments.ClosetFragment;
import com.example.myapplication.CodyFragments.CodyFragment;
import com.example.myapplication.MyFragments.MyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ClosetFragment closetFragment;
    CodyFragment codyFragment;
    BoardFragment boardFragment;
    MyFragment myFragment;
    BoardWriteFragment boardWriteFragment;
    BoardReadFragment boardReadFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        closetFragment = new ClosetFragment();
        codyFragment = new CodyFragment();
        boardFragment = new BoardFragment();
        myFragment = new MyFragment();
        boardWriteFragment = new BoardWriteFragment();
        boardReadFragment = new BoardReadFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, closetFragment).commitAllowingStateLoss();
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_closet:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, closetFragment).commitAllowingStateLoss();
                        return true;

                    case R.id.navigation_cody:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, codyFragment).commitAllowingStateLoss();
                        return true;

                    case R.id.navigation_board:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, boardFragment).commitAllowingStateLoss();
                        return true;

                    case R.id.navigation_my:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, myFragment).commitAllowingStateLoss();
                        return true;

                    default:
                        return false;
                }
            }
        });

    }
}
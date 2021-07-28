package com.example.smartattendance;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TestActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private FragmentTransaction transaction;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        //바텀네비게이션 부분.
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //첫 화면 설정.
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new Fragment1()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull    MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_fragment1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Fragment1()).commit();
                        break;
                    case R.id.item_fragment2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Fragment2()).commit();
                        break;
                }
                return true;
            }
        });

    }


}

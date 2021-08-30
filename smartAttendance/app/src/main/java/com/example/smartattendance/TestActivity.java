package com.example.smartattendance;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

public class TestActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    DatabaseReference mDatebase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        String myID = intent.getStringExtra("myID");
        String myComp = intent.getStringExtra("myComp");
        String plzShowMeError = "ERROR";

        //바텀네비게이션 부분.
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Fragment attendFragment = new AttendFragment();
        Fragment searchFragment = new SearchFragment();

        Bundle userInfoBundle = new Bundle();
        userInfoBundle.putString("myID",myID);
        userInfoBundle.putString("myComp",myComp);

        attendFragment.setArguments(userInfoBundle);
        searchFragment.setArguments(userInfoBundle);

        //첫 화면 설정.
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, attendFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull    MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_fragment1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, attendFragment).commit();
                        break;
                    case R.id.item_fragment2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, searchFragment).commit();
                        break;
                }
                return true;
            }
        });

    }


}

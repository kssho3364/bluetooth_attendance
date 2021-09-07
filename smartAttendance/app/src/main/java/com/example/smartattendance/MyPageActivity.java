package com.example.smartattendance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyPageActivity extends AppCompatActivity {
    private TextView viewID_tv, viewName_tv, viewEmail_tv, showName_tv;
    private Button modifyUserData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        viewID_tv = findViewById(R.id.showProfileID_tv);
        viewEmail_tv = findViewById(R.id.showProfileEmail_tv);
        viewName_tv = findViewById(R.id.showProfileName_tv);
        showName_tv = findViewById(R.id.profile_name_tv);
        modifyUserData = findViewById(R.id.modifyUserData_bt);

        viewID_tv.setText(((UserInfoData)getApplication()).getMyID());
        viewName_tv.setText(((UserInfoData)getApplication()).getMyName());
        showName_tv.setText(((UserInfoData)getApplication()).getMyName());
//        viewEmail_tv.setText(((UserInfoData)getApplication()).getMyEmail()); 이메일은 지금 없음

        modifyUserData.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(),ModifyUserDataActivity.class);
            startActivity(intent);
        });

    }
}

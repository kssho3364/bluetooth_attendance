package com.example.smartattendance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingCompActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button selectOtherComp_bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingcomp);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        selectOtherComp_bt = findViewById(R.id.select_otherComp_bt);

        //리스트뷰 넣고, SelectCompActivity로 인텐트, 키값 번들로 보내서 특정키값이면 다른인텐트로 넘어가게. 기존 회사 +추가버튼

        selectOtherComp_bt.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(),ConfirmActivity.class);
            intent.putExtra("key","1");
        });


    }
}

package com.example.smartattendance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SettingCompActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button selectOtherComp_bt;
    private ListView settingComp_listview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingcomp);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        selectOtherComp_bt = findViewById(R.id.select_otherComp_bt);
        settingComp_listview = findViewById(R.id.settingcomp_listview);

        List<String> data = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,data);

        settingComp_listview.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        for (int a=0; a <10; a++){
            data.add(""+a);
        }
        adapter.notifyDataSetChanged();

        //리스트뷰 넣고, SelectCompActivity로 인텐트, 키값 번들로 보내서 특정키값이면 다른인텐트로 넘어가게. 기존 회사 +추가버튼

        selectOtherComp_bt.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(),ConfirmActivity.class);
            intent.putExtra("key","1");
        });


    }
}

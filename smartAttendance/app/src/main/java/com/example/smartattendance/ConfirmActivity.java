package com.example.smartattendance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConfirmActivity extends AppCompatActivity {

    private Button selectComp_bt, confirm_bt;
    private TextView showComp_tv;
    private DatabaseReference mDatabase;
    private String myID, myComp, selectCompName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Intent getIntent = getIntent();
//        myID = intent.getStringExtra("myID");
//        myComp = intent.getStringExtra("myComp");
//        Log.d("aaaaaaaaaaaaa",""+myID);

        myID = ((UserInfoData)getApplication()).getMyID();

        selectCompName = getIntent.getStringExtra("selectCompName");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        selectComp_bt = findViewById(R.id.selectComp_bt);
        showComp_tv = findViewById(R.id.showComp_tv);
        confirm_bt = findViewById(R.id.confirm_bt);

        showComp_tv.setText(selectCompName);

        selectComp_bt.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(),SelectCompActivity.class);
            startActivity(intent);
            finish();
        });
        confirm_bt.setOnClickListener(v ->{
            mDatabase.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (dataSnapshot.getKey().equals(showComp_tv.getText().toString())){
                            Log.d("aaaaaa",""+myID);
                            mDatabase.child("User").
                                    child(myID).
                                    child("COMP").
                                    setValue(selectCompName);
                            // 데이터 전송 실패시 대처방안 마련해야함.
                            Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

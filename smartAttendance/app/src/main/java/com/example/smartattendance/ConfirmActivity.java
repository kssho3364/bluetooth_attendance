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

        Intent intent = getIntent();
        myID = intent.getStringExtra("myID");
        myComp = intent.getStringExtra("myComp");
        Log.d("aaaaaaaaaaaaa",""+myID);
        selectCompName = intent.getStringExtra("selectCompName");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        selectComp_bt = findViewById(R.id.selectComp_bt);
        showComp_tv = findViewById(R.id.showComp_tv);
        confirm_bt = findViewById(R.id.confirm_bt);

        showComp_tv.setText(selectCompName);

        selectComp_bt.setOnClickListener(v ->{
            Intent intent1 = new Intent(getApplicationContext(),SelectCompActivity.class);
            intent1.putExtra("myID",myID);
            intent1.putExtra("myComp",myComp);
            startActivity(intent1);
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
                                    child(intent.getStringExtra("myID")).
                                    child("COMP").
                                    setValue(showComp_tv.getText().toString());
                            // 데이터 전송 실패시 대처방안 마련해야함.
                            // 액티비티 전환시 데이터 유지하는 방법도 마련해야함.. putExtra 넘많이씀.
                            Intent intent2 = new Intent(getApplicationContext(),TestActivity.class);
                            intent2.putExtra("myID",myID);
                            intent2.putExtra("myComp",myComp);
                            startActivity(intent2);
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

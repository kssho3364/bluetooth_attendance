package com.example.smartattendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.atomic.AtomicInteger;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private String code = "";

    private Button request_bt, overlap_bt;
    private EditText setMyName_edit, insertCode_edit, id_edit, pw_edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        request_bt = findViewById(R.id.pullRequest_bt);
        setMyName_edit = findViewById(R.id.setMyName_edit);
        insertCode_edit = findViewById(R.id.insertCode_edit);
        overlap_bt = findViewById(R.id.overlap_bt);
        id_edit = findViewById(R.id.id_edit);
        pw_edit = findViewById(R.id.pw_edit);

        request_bt.setVisibility(View.INVISIBLE);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // ID 중복확인
        overlap_bt.setOnClickListener(v -> {
            String ID = id_edit.getText().toString();
            mDatabase.child("EMPLOYEES").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    int count = 0;
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        DAOinfo daoinfo = dataSnapshot.getValue(DAOinfo.class);
                        if(daoinfo.getID().equals(ID)){
                            count++;
                            break;
                            //중복된게 있다면 카운트가 하나씩 올라가서 for문 탈출시 중복여부 확인코딩해야함
                        }
                    }
                    // 데이터베이스에 저장된 id값과 중복된것이 있을경우
                    if(count > 0){
                        Toast.makeText(RegisterActivity.this, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                        request_bt.setVisibility(View.INVISIBLE);
                        Log.d("asd","중복된 값 있음.");
                    }
                    //중복된 것이 없을경우
                    else{
                        Toast.makeText(RegisterActivity.this, "사용할 수 있는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        request_bt.setVisibility(View.VISIBLE);
                        Log.d("asd","중복된 값 없음");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        });

        request_bt.setOnClickListener(v -> {
            String info[] = new String[3];

            info[0] = id_edit.getText().toString();
            info[1] = pw_edit.getText().toString();
            info[2] = setMyName_edit.getText().toString();
            mDatabase.child("EMPLOYEES").child("ID").setValue(info[0]);
            mDatabase.child("EMPLOYEES").child("PW").setValue(info[1]);
            mDatabase.child("EMPLOYEES").child("NAME").setValue(info[2]);

            Toast.makeText(RegisterActivity.this,"회원가입 완료!",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

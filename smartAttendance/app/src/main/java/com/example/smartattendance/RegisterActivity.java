package com.example.smartattendance;

import android.content.SharedPreferences;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private String code = "";

    private Button request_bt, pullRequest_bt;
    private EditText setMyName_edit, insertCode_edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        request_bt = findViewById(R.id.request_bt);
        setMyName_edit = findViewById(R.id.setMyName_edit);
        insertCode_edit = findViewById(R.id.insertCode_edit);
        pullRequest_bt = findViewById(R.id.pullRequest_bt);

        pullRequest_bt.setOnClickListener(v->{
            getCode();
        });

        request_bt.setOnClickListener(v -> {
            if (code.equals(insertCode_edit.getText().toString())){
                //코드가 일치하면
                SharedPreferences sf = getSharedPreferences("myName",MODE_PRIVATE);
                SharedPreferences.Editor editor = sf.edit();
                editor.putString("name",setMyName_edit.getText().toString());
                editor.commit();
                Toast.makeText(RegisterActivity.this,"안정적인 실행을 위해 앱을 다시 시작해주세요",Toast.LENGTH_LONG).show();
                finish();
            }
            else{
                //일치하지 않으면
                Toast.makeText(RegisterActivity.this,"인증번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCode(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("CODE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                code = snapshot.getValue().toString();
                //코드가없을때 예외처리를 해주는게 좋을것같음
                Toast.makeText(RegisterActivity.this,"코드를 입력해주세요!",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this,"불러오기 실패!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

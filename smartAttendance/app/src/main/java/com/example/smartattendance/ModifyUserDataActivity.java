package com.example.smartattendance;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ModifyUserDataActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText password_et, repassword_et;
    private Button changeInfo_bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_userdata);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        password_et = findViewById(R.id.password_et);
        repassword_et = findViewById(R.id.Repassword_et);
        changeInfo_bt = findViewById(R.id.changeInfo_bt);

        changeInfo_bt.setOnClickListener(v->{
            if (password_et.getText().toString().equals(repassword_et.getText().toString())){
                mDatabase.child("User").child(((UserInfoData)getApplication()).getMyID()).child("PW").setValue(password_et.getText().toString());
                Toast.makeText(ModifyUserDataActivity.this, "변경되었습니다.",Toast.LENGTH_SHORT).show();
                ((UserInfoData)getApplication()).setMyPW(password_et.getText().toString());
                Log.d("change pw",""+((UserInfoData)getApplication()).getMyPW());
                finish();
            }
            else{
                Toast.makeText(ModifyUserDataActivity.this, "비밀번호가 일치하지않습니다.",Toast.LENGTH_SHORT).show();
            }
        });


    }
}

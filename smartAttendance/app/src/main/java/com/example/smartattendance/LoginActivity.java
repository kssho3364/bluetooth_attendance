package com.example.smartattendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private DatabaseReference mDatabase;

    private static final int PERMISSIONS_REQUEST_RESULT = 10;

    private Button register_bt, login_bt, test_bt1;
    private EditText id_et, pw_et;
    private CheckBox autoLogin_cb;
    private SharedPreferences autoLogPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("상태","크리에이트");

        register_bt = findViewById(R.id.register_bt);
        login_bt = findViewById(R.id.login_bt);
        id_et = findViewById(R.id.ID_et);
        pw_et = findViewById(R.id.PW_et);
        test_bt1 = findViewById(R.id.test_bt1);
        autoLogin_cb = findViewById(R.id.autoLogin_cb);

        //자동로그인을 위한 쉐어드프리퍼런스
        autoLogPreferences = getSharedPreferences("autoLogin",MODE_PRIVATE);

        //블루투스 호환기기인지 판별을 위한 어댑터.
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //파이어베이스 연동.
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //내부저장소에 인증받은 이력이 있는지 검사(ID코드 혹은 이름을 서버에서 받아왔는지) 후 변수 저장
        autoLogPreferences = getSharedPreferences("autoLogin",MODE_PRIVATE);

        // 실행시, 위치권한 설정, 이미 허용되어있으면 넘어감
        // 권한을 거부할때 기능 넣어야함.....***************
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_RESULT);

        //블루투스 호환 기기인지 판별
        if (bluetoothAdapter == null) {
            Toast.makeText(this,"블루투스를 지원하지 않는 기기입니다,",Toast.LENGTH_SHORT).show();
            finish();
        }
        // 테스트
        test_bt1.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),ConfirmActivity.class);
            startActivity(intent);
            finish();
        });

        //회원가입 페이지로 이동
        register_bt.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
            startActivity(intent);
            finish();
        });
        //로그인 버튼 이벤트
        login_bt.setOnClickListener(v-> {
            doLogin(id_et.getText().toString(), pw_et.getText().toString());
        });
    }

    public void doLogin(String id, String pw) {
        if (!id_et.getText().toString().equals(null) && !pw_et.getText().toString().equals(null)) {
            mDatabase.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    int snapshotSize = 0;
                    int count = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        snapshotSize++;
                        DAOinfo daoinfo = dataSnapshot.getValue(DAOinfo.class);

                        if (daoinfo.getID().equals(id)) {
                            //ID와 PW가 전부 일치할때.
                            if (daoinfo.getPW().equals(pw)) {
                                //Application을 이용한 전역변수 선언.
                                ((UserInfoData) getApplication()).setMyID(daoinfo.getID());
                                ((UserInfoData) getApplication()).setMyPW(daoinfo.getPW());
                                ((UserInfoData) getApplication()).setMyComp(daoinfo.getCOMP());
                                ((UserInfoData) getApplication()).setMyName(daoinfo.getNAME());

                                if (daoinfo.getCOMP().equals("null")) {
                                    //최초 로그인  (등록된 회사가 없을때, 최초 회원가입시, 자동으로 회사이름은 null이 됨.)
                                    Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                } else {
                                    //메인액티비티로가야함
                                    Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        } else {
                            //일치하는 아이디가 없을때 토스트메세지 작성해야함..
                            count++;
                        }
                    }
                    if (count == snapshotSize) {
                        Toast.makeText(LoginActivity.this, "일치하는 아이디가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(LoginActivity.this, "서버와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(LoginActivity.this,"입력해라",Toast.LENGTH_SHORT).show();
        }
    }
    public void setAutoLogin(){
        SharedPreferences.Editor editor = autoLogPreferences.edit();
        editor.putString("ID",id_et.getText().toString());
        editor.putString("PW",pw_et.getText().toString());
        editor.commit();
    }
    public void isAutoLoginCheck(){
        autoLogin_cb.isChecked();
    }
}
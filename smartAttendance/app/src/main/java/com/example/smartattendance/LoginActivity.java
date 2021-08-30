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

    private String MY_NAME = "";

    private Button register_bt, login_bt, test_bt1;
    private EditText id_et, pw_et;

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

        ArrayList<String> arrayListy = new ArrayList<>();

        //블루투스 호환기기인지 판별을 위한 어댑터.
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //파이어베이스 연동.
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //내부저장소에 인증받은 이력이 있는지 검사(ID코드 혹은 이름을 서버에서 받아왔는지) 후 변수 저장
        SharedPreferences sharedPreferences = getSharedPreferences("myName",MODE_PRIVATE);

        //받아온 값이 없다면 공백으로 저장됨.
        MY_NAME = sharedPreferences.getString("name","");

        // 실행시, 위치권한 설정, 이미 허용되어있으면 넘어감
        // 권한을 거부할때 기능 넣어야함.....***************
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_RESULT);

        //블루투스 호환 기기인지 판별
        if (bluetoothAdapter == null) {
            Toast.makeText(this,"블루투스를 지원하지 않는 기기입니다,",Toast.LENGTH_SHORT).show();
            finish();
        }
        //내부저장소에 사용자의 이름이 저장되어있다면, 메인액티비티로 넘어감(자동 로그인)
        if (!MY_NAME.equals("")){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
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
            String id = id_et.getText().toString();
            String pw = pw_et.getText().toString();

            mDatabase.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    int snapshotSize = 0;
                    int count = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        snapshotSize++;
                        DAOinfo daoinfo = dataSnapshot.getValue(DAOinfo.class);

                        Log.d("asd", "" + daoinfo.getID());

                        if(daoinfo.getID().equals(id)){
                            Log.d("asd", "if탐?");
                            if(daoinfo.getPW().equals(pw)) {

                                String myName = daoinfo.getNAME();
                                String myComp = daoinfo.getCOMP();
                                String myID = daoinfo.getID();


                                if (daoinfo.getCOMP().equals("null")){
                                    //최초 로그인  (등록된 회사가 없을때)
                                    Intent intent = new Intent(getApplicationContext(),ConfirmActivity.class);
                                    intent.putExtra("myID",myID);
                                    intent.putExtra("myComp",myComp);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                                else{
                                    //메인액티비티로가야함
                                    Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                                    intent.putExtra("myID",myID);
                                    intent.putExtra("myComp",myComp);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }

                            }
                            else{
                                Toast.makeText(LoginActivity.this, "비밀번호가 다릅니다.",Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        else{
                            //일치하는 아이디가 없을때 토스트메세지 작성해야함..
                            count++;
                        }
                    }
                    if (count == snapshotSize){
                        Toast.makeText(LoginActivity.this, "일치하는 아이디가 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(LoginActivity.this, "서버와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

//            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(intent);
//            finish();
                // 파이어베이스 데이터에서 아이디 먼저 검색 후, 일치하는 아이디가 있으면 그 아이디에 저장된 비밀번호 조회 후 일치 검사.
//            mDatabase.child("attend").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    Log.d("asdasdasdasdass","pppppppppppp"+snapshot.getValue());
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    Log.d("asdasdasdasdass","fail");
//                }
//            });



            /*모두 일치했을 경우 메인액티비티로 이동.
            if(true){
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }*/
            });
        });
    }
}
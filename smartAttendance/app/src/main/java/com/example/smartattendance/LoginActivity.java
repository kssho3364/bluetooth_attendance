package com.example.smartattendance;

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

public class LoginActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private static final int PERMISSIONS_REQUEST_RESULT = 10;


    private String MY_NAME = "";

    private Button register_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("상태","크리에이트");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        register_bt = findViewById(R.id.register_bt);

        //내부저장소에 인증받은 이력이 있는지 검사(ID코드 혹은 이름을 서버에서 받아왔는지) 후 변수 저장
        SharedPreferences sharedPreferences = getSharedPreferences("myName",MODE_PRIVATE);
        //받아온 값이 없다면 공백으로 저장됨.
        MY_NAME = sharedPreferences.getString("name","");

        // 실행시, 위치권한 설정, 이미 허용되어있으면 넘어감
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_RESULT);

        //블루투스 호환 기기인지 판별
        if (bluetoothAdapter == null) {
            Toast.makeText(this,"블루투스를 지원하지 않는 기기입니다,",Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!MY_NAME.equals("")){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        register_bt.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
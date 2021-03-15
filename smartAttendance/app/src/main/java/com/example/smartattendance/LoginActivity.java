package com.example.smartattendance;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private LocationManager locationManager;

    private static final int PERMISSIONS_REQUEST_RESULT = 10;
    private static final int REQUEST_ENABLE_BT = 3;

    private Button register_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        register_bt = findViewById(R.id.register_bt);

        //내부저장소에 인증받은 이력이 있는지 검사(ID코드 혹은 이름을 서버에서 받아왔는지) 후 변수 저장
        SharedPreferences sharedPreferences = getSharedPreferences("myName",MODE_PRIVATE);
        //받아온 값이 없다면 공백으로 저장됨.
        String myName = sharedPreferences.getString("name","");

        // 실행시, 위치권한 설정, 이미 허용되어있으면 넘어감
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_RESULT);

        //블루투스 호환 기기인지 판별
        if (bluetoothAdapter == null) {
            Toast.makeText(this,"블루투스를 지원하지 않는 기기입니다,",Toast.LENGTH_SHORT).show();
            finish();
        }

        //쉐어드프리퍼런스
        if (!myName.equals("")){
            setDeviceForAuto();
        }

        register_bt.setOnClickListener(v -> {
            setDeviceForNewAccount();
        });
    }
    public void setDeviceForAuto(){
        //블루투스를 켜지 않았다면 실행
        if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            finish();
        }
        //위치를 켜지 않았다면 실행
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("위치 서비스를 켜시겠습니까?");
            builder.setPositiveButton("예", (dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("아니오",(dialog, which) -> {
                Toast.makeText(getApplicationContext(),"위치 서비스를 켜야합니다.",Toast.LENGTH_SHORT).show();
                finish();
            });
            builder.create().show();
        }
        //위치와 블루투스를 모두 켰다면 실행
        if (bluetoothAdapter.isEnabled() && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void setDeviceForNewAccount(){
        //블루투스를 켜지 않았다면 실행
        if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        //위치를 켜지 않았다면 실행
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("위치 서비스를 켜시겠습니까?");
            builder.setPositiveButton("예", (dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            });
            builder.setNegativeButton("아니오",(dialog, which) -> {
                Toast.makeText(getApplicationContext(),"위치 서비스를 켜야합니다.",Toast.LENGTH_SHORT).show();
            });
            builder.create().show();
        }
        //위치와 블루투스를 모두 켰다면 실행
        if (bluetoothAdapter.isEnabled() && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Intent intent1 = new Intent(getApplicationContext(),RegisterActivity.class);
            startActivity(intent1);
            finish();
        }
    }
}
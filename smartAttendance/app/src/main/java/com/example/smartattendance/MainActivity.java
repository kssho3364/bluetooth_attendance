package com.example.smartattendance;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private static final int REQEST_ENABLE_LOCATION = 10;
    private static final int REQUEST_ENABLE_BT = 3;
    private String DEVICE_NAME = "";
    private static final String ATTENDANCE = "출근";
    private static final String OFFWORK = "퇴근";
    private static final int ATTENDANCE_CODE = 3000;
    private static final int OFFWORK_CODE = 3001;
    private int checkAtt = 0;

    private LocationManager locationManager;
    private BluetoothAdapter bluetoothAdapter;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private DatabaseReference mDatabase;

    private Executor executor;

    private Button attendance_bt, offWork_bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("DEVICE_NAME").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //코드가없을때 예외처리를 해주는게 좋을것같음
                DEVICE_NAME = snapshot.getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        attendance_bt = findViewById(R.id.attendance_bt);
        offWork_bt = findViewById(R.id.offWork_bt);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        executor = ContextCompat.getMainExecutor(this);

        attendance_bt.setOnClickListener(v->{
            checkAtt = ATTENDANCE_CODE;
            setDevice();
        });

        offWork_bt.setOnClickListener(v->{
            checkAtt = OFFWORK_CODE;
            biometricPrompt.authenticate(promptInfo);
        });
    }
    /////블루투스 브로드캐스트리시버
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = getSharedPreferences("myName",MODE_PRIVATE);
            String myName = sharedPreferences.getString("name","");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("태그",""+device.getName());

                //getName() 값이 null 이면 .equals 를 사용할 때 오류가 난다.
                //따라서 device 주소와 비교하기 전에 null인지 먼저 비교해본다.
                if(device.getName() != null){
                    if (device.getName().equals(DEVICE_NAME)) {
                        bluetoothAdapter.cancelDiscovery();
                        Log.d("태그", "찾음");
                        //여기서 서버통신, 출근.퇴근 코드 쏘기
                        if (checkAtt == ATTENDANCE_CODE){
                            sendToFirebase(myName,ATTENDANCE);
                        }else if(checkAtt == OFFWORK_CODE){
                            sendToFirebase(myName,OFFWORK);
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    //파이어베이스 부분
    private void sendToFirebase(String name, String kind) {

        //현재 시간과 날짜
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String mDate = mDateFormat.format(date);
        SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String mTime = mTimeFormat.format(date);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //데이터를 넣는 부분.
        mDatabase.child(mDate).child(name).child(kind).setValue(mTime)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 저장성공시
                        Toast.makeText(MainActivity.this, ""+kind+" 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 저장실패시
                        Toast.makeText(MainActivity.this, "에러", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void setDevice(){
        //블루투스를 켜지 않았다면 실행
        if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        //위치를 켜지 않았다면 실행
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("위치 서비스를 켜시겠습니까?");
            builder.setPositiveButton("예", (dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent,REQEST_ENABLE_LOCATION);
            });
            builder.setNegativeButton("아니오",(dialog, which) -> {
                Toast.makeText(getApplicationContext(),"위치 서비스를 켜야합니다.",Toast.LENGTH_SHORT).show();
            });
            builder.create().show();
        }
        //위치와 블루투스를 모두 켰다면 실행
        if (bluetoothAdapter.isEnabled() && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            doBiometric();
        }
    }

    public void doBiometric(){
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            //취소, 에러일 경우
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            //성공했을 경우
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                //블루투스 스캔 시작
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.startDiscovery();
            }

            @Override //실패했을 경우
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        //지문인식 다이얼로그 창
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("지문 인증")
                .setSubtitle("")
                .setNegativeButtonText("취소")
                .setDeviceCredentialAllowed(false)
                .build();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(receiver, filter);

        filter.addAction(BluetoothDevice.ACTION_FOUND);

        biometricPrompt.authenticate(promptInfo);
    }
}

package com.example.smartattendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectCompActivity extends AppCompatActivity {

    private DatabaseReference mDatebase;
    private ListView compListView;
    private Button search_bt;
    private EditText searchComp_et;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcomp);

        compListView =findViewById(R.id.CompListView);
        search_bt = findViewById(R.id.search_bt);
        searchComp_et = findViewById(R.id.searchComp_et);

        Intent intent = getIntent();
        String myName = intent.getStringExtra("myName");

        mDatebase = FirebaseDatabase.getInstance().getReference();

        List<String> data = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,data);
        compListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        search_bt.setOnClickListener(v->{

            mDatebase.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    data.clear();
                    int countComp = 0;
                    String compName =  searchComp_et.getText().toString();
                    Log.d("bbbbbbbbbbbbb",""+compName);

                    if (compName.equals("")) {
                        Toast.makeText(SelectCompActivity.this,"???????????? ??????????????????",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.getKey().contains(compName)) {
                                Log.d("asdasd", "if??????????");
                                data.add(dataSnapshot.getKey());
                                countComp++;
                            }
                        }
                        Log.d("asdasd", "????????????????");
                        if (countComp == 0) {
                            Toast.makeText(SelectCompActivity.this, "???????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();//???????????? ??????
//                      compListView.setAdapter(adapter); ???????????? ?????? ????????? ??????

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        compListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SelectCompActivity.this, data.get(position),Toast.LENGTH_SHORT).show();
                //data.get(position)
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectCompActivity.this);
                builder.setTitle(data.get(position));
                builder.setMessage("?????????????");
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //????????????
                        Intent intent = new Intent(getApplicationContext(),ConfirmActivity.class);
                        intent.putExtra("selectCompName",data.get(position));
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //?????? ??????
                    }
                });
                builder.create().show();

            }
        });
    }
}

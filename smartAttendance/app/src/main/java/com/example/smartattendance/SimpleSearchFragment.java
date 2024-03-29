package com.example.smartattendance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SimpleSearchFragment extends Fragment {

    private CalendarView simpleSearch_cv;
    private TextView attend_tv, offWork_tv;
    private DatabaseReference mDatabase;
    private String myComp, myID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_simple_search_view,container,false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        simpleSearch_cv = (CalendarView) view.findViewById(R.id.simpleSearch_cv);
        attend_tv = (TextView) view.findViewById(R.id.attend_tv);
        offWork_tv = (TextView) view.findViewById(R.id.offWork_tv);

        myID = ((UserInfoData)getActivity().getApplication()).getMyID();
        myComp = ((UserInfoData)getActivity().getApplication()).getMyComp();

        simpleSearch_cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                Log.d("dddd",""+selectDate);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(year+"년"+month+"월"+dayOfMonth+"일\n검색하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.child("Company").child(myComp).child("Attend").child(myID).child(selectDate).child("출근").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Log.d("dddd",""+snapshot.getValue());
                                try {
                                    String getTime = snapshot.getValue().toString();
                                    String splitTime[] = getTime.split(":");
                                    attend_tv.setText(splitTime[0]+"시 "+splitTime[1]+"분");
                                }catch (Exception E){
                                    attend_tv.setText("정보 없음.");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        mDatabase.child("Company").child(myComp).child("Attend").child(myID).child(selectDate).child("퇴근").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Log.d("dddd",""+snapshot.getValue());
                                try {
                                    String getTime = snapshot.getValue().toString();
                                    String splitTime[] = getTime.split(":");
                                    offWork_tv.setText(splitTime[0]+"시 "+splitTime[1]+"분");
                                }catch (Exception e){
                                    offWork_tv.setText("정보 없음.");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });

        return view;
    }
}

package com.example.smartattendance;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.lollypop.be.model.User;

public class ComplexSearchFragment extends Fragment {
    private TextView toDate_tv, fromDate_tv;
    private Button cancel_bt,accept_bt, complexSearch_bt;
    private Dialog selectDateDialog;
    private NumberPicker selectDay_np,selectYear_np,selectMonth_np;
    private ListView complexListView;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_complex_search_view,container,false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        selectDateDialog = new Dialog(getActivity());
        selectDateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectDateDialog.setContentView(R.layout.select_date_dialog);

        toDate_tv = view.findViewById(R.id.to_date_tv);
        fromDate_tv = view.findViewById(R.id.from_date_tv);
        cancel_bt = selectDateDialog.findViewById(R.id.cancel_bt);
        accept_bt = selectDateDialog.findViewById(R.id.accept_bt);
        selectDay_np = selectDateDialog.findViewById(R.id.select_date_np);
        selectMonth_np = selectDateDialog.findViewById(R.id.select_month_np);
        selectYear_np = selectDateDialog.findViewById(R.id.select_year_np);
        complexSearch_bt = view.findViewById(R.id.complex_search_bt);

        complexListView = view.findViewById(R.id.complex_search_listview);

        List<String> data = new ArrayList<>();
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,data);
//        adapter.notifyDataSetChanged();

        ComplexListViewAdapter adapter = new ComplexListViewAdapter();


        complexSearch_bt.setOnClickListener(v->{
            adapter.ItemClear();
            mDatabase.child("Company").child(((UserInfoData)getActivity().getApplication()).getMyComp()).child("Attend").child(((UserInfoData)getActivity().getApplication()).getMyID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String baseDate = dataSnapshot.getKey().replace("-","");
                        String toDate = toDate_tv.getText().toString().replace("-","");
                        String fromDate = fromDate_tv.getText().toString().replace("-","");

                        if (Integer.parseInt(baseDate) >= Integer.parseInt(fromDate) && Integer.parseInt(baseDate) <= Integer.parseInt(toDate)){
                                Log.d("aaaaa",snapshot.child(dataSnapshot.getKey()).child("출근").getValue().toString());
                                String attend;
                                String offWork;
                                try {
                                    attend = snapshot.child(dataSnapshot.getKey()).child("출근").getValue().toString();
                                }catch (Exception e){
                                    attend = "정보없음";
                                }
                                try {
                                    offWork = snapshot.child(dataSnapshot.getKey()).child("퇴근").getValue().toString();
                                }catch (Exception e){
                                    offWork = "정보 없음";
                                }

                            adapter.addItem(dataSnapshot.getKey(), attend, offWork);
                            }
                    }
                    complexListView.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });





        for (int i=0;i<10;i++){
            data.add("리스트"+i);
        }

        setNumberPicker();

        fromDate_tv.setOnClickListener(v -> {
            showDialog("from");
        });
        toDate_tv.setOnClickListener(v->{
            showDialog("to");
        });
        //리스트뷰 추가.

        return view;
    }

    public void dataSetting(String date, String attend, String offwork){

        ComplexListViewAdapter adapter = new ComplexListViewAdapter();


        adapter.addItem(date, attend, offwork);



        complexListView.setAdapter(adapter);
    }

    public void setNumberPicker(){
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (toDate_tv.getText().toString().equals("TextView") || fromDate_tv.getText().toString().equals("TextView")){
            toDate_tv.setText(simpleDateFormat.format(today));
            fromDate_tv.setText(simpleDateFormat.format(today));
        }
    }

    public void showDialog(String kind){
        Date today = new Date(System.currentTimeMillis());
        String splitString = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        selectMonth_np.setMaxValue(12); selectMonth_np.setMinValue(01);
        selectYear_np.setMaxValue(2030); selectYear_np.setMinValue(2010);
        selectDay_np.setMaxValue(31); selectDay_np.setMinValue(01);

        if(kind.equals("from")){
            splitString = fromDate_tv.getText().toString();
        }else{
            splitString = toDate_tv.getText().toString();
        }
        String Date[];
        Date = splitString.split("-");
        selectYear_np.setValue(Integer.parseInt(Date[0]));
        selectMonth_np.setValue(Integer.parseInt(Date[1]));
        selectDay_np.setValue(Integer.parseInt(Date[2]));

        selectDateDialog.show();

        accept_bt.setOnClickListener(v->{
            String formatDate = String.format("%d-%02d-%02d",selectYear_np.getValue(),selectMonth_np.getValue(),selectDay_np.getValue());
            if (kind.equals("from")){
                fromDate_tv.setText(formatDate);
            }else{
                toDate_tv.setText(formatDate);
            }
            selectDateDialog.dismiss();
        });
        cancel_bt.setOnClickListener(v->{
            selectDateDialog.dismiss();
        });
    }
}

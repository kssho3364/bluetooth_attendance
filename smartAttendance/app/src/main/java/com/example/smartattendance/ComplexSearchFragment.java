package com.example.smartattendance;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ComplexSearchFragment extends Fragment {
    private TextView toDate_tv, fromDate_tv;
    private Button cancel_bt,accept_bt;
    private Dialog selectDateDialog;
    private NumberPicker selectDay_np,selectYear_np,selectMonth_np;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_complex_search_view,container,false);

        toDate_tv = view.findViewById(R.id.to_date_tv);
        fromDate_tv = view.findViewById(R.id.from_date_tv);
        selectDateDialog = new Dialog(getActivity());
        selectDateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectDateDialog.setContentView(R.layout.select_date_dialog);
        cancel_bt = selectDateDialog.findViewById(R.id.cancel_bt);
        accept_bt = selectDateDialog.findViewById(R.id.accept_bt);

        selectDay_np = selectDateDialog.findViewById(R.id.select_date_np);
        selectMonth_np = selectDateDialog.findViewById(R.id.select_month_np);
        selectYear_np = selectDateDialog.findViewById(R.id.select_year_np);


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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String splitString = simpleDateFormat.format(today);
        String date[];
        date = splitString.split("-");

        selectMonth_np.setMaxValue(12); selectMonth_np.setMinValue(01); selectMonth_np.setValue(Integer.parseInt(date[1]));
        selectYear_np.setMaxValue(2030); selectYear_np.setMinValue(2010); selectYear_np.setValue(Integer.parseInt(date[0]));
        selectDay_np.setMaxValue(31); selectDay_np.setMinValue(01); selectDay_np.setValue(Integer.parseInt(date[2]));
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

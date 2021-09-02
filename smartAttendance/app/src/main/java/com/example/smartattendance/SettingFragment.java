package com.example.smartattendance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {

    Button notice_bt, myPage_bt, settingComp_bt, setting_bt, logOut_bt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container,false);

        notice_bt = (Button) view.findViewById(R.id.notice_bt);
        myPage_bt = (Button) view.findViewById(R.id.myPage_bt);
        settingComp_bt = (Button) view.findViewById(R.id.settingComp_bt);
        setting_bt = (Button) view.findViewById(R.id.setting_bt);
        logOut_bt = (Button) view.findViewById(R.id.logOut_bt);


        return view;
    }
}

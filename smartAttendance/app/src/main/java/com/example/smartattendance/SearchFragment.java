package com.example.smartattendance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchFragment extends Fragment {

    private String myComp, myID;
    private BottomNavigationView TopNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        //출퇴근 검색기능 액티비티.
        TopNavigationView = (BottomNavigationView)view.findViewById(R.id.TopNavigationView);

        Fragment fragment_simple_search_view = new SimpleSearchFragment();
        Fragment fragment_complex_search_view = new ComplexSearchFragment();

        getChildFragmentManager().beginTransaction().add(R.id.search_frameLayout, fragment_simple_search_view).commit();

        Log.d("dddd","왔나?");
        TopNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_simple:
                        getChildFragmentManager().beginTransaction().replace(R.id.search_frameLayout,fragment_simple_search_view).commit();
                        break;
                    case R.id.item_complex:
                        getChildFragmentManager().beginTransaction().replace(R.id.search_frameLayout,fragment_complex_search_view).commit();
                        break;
                }
                return true;
            }
        });

        return view;
    }

}



